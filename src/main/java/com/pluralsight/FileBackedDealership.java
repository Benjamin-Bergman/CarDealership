/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * Decorates a {@link Dealership} with file-saving semantics.
 */
public final class FileBackedDealership implements Dealership {
    private final File filePath;
    private final Dealership wrapped;
    private final String displayName, address, phone;

    /**
     * Creates a new FileBackedDealership.
     * Note that this replaces the contents of {@code wrapped} with the contents of the file.
     *
     * @param wrapped  The Dealership to decorate
     * @param filePath The File to load from and save to
     */
    public FileBackedDealership(Dealership wrapped, File filePath) {
        this.wrapped = wrapped;
        this.filePath = filePath;

        wrapped.clear();
        try (var fr = new FileReader(filePath);
             var br = new BufferedReader(fr)) {
            var header = br.readLine();

            var parts = header.split("\\|");
            if (parts.length == 3) {
                displayName = parts[0];
                address = parts[1];
                phone = parts[2];
            } else if (
                isValid(wrapped.getDisplayName())
                && isValid(wrapped.getAddress())
                && isValid(wrapped.getPhone())
            ) {
                displayName = wrapped.getDisplayName();
                address = wrapped.getAddress();
                phone = wrapped.getPhone();
            } else throw new IOException("Bad file header when reading $filePath");

            wrapped.addAll(
                br.lines()
                    .map(FileBackedDealership::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
            );
        }
    }

    private static boolean isValid(String str) {
        return str != null && !str.isEmpty();
    }

    private static Vehicle parseLine(String s) {
        if (s == null)
            return null;
        var parts = s.split("\\|");
        if (parts.length != 8)
            return null;
        var vin = parseInt(parts[0]);
        if (vin.isEmpty())
            return null;
        var year = parseInt(parts[1]);
        if (year.isEmpty())
            return null;
        var odometer = parseInt(parts[6]);
        if (odometer.isEmpty())
            return null;
        var price = parseDouble(parts[7]);
        if (price.isEmpty())
            return null;

        return new Vehicle(vin.getAsInt(), year.getAsInt(),
            parts[2], parts[3],
            parts[4], parts[5],
            odometer.getAsInt(), price.getAsDouble());
    }

    private static OptionalInt parseInt(String s) {
        try {
            return OptionalInt.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }

    private static OptionalDouble parseDouble(String s) {
        try {
            return OptionalDouble.of(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return OptionalDouble.empty();
        }
    }

    @SuppressWarnings("FeatureEnvy")
    private static String serialize(Vehicle v) {
        return "%d|%d|%s|%s|%s|%s|%d|%.2f".formatted(
            v.vin(), v.year(),
            v.make(), v.model(),
            v.vehicleType(), v.color(),
            v.odometer(), v.price());
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return wrapped.getAllVehicles();
    }

    @Override
    public void add(Vehicle vehicle) {
        wrapped.add(vehicle);
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.newLine();
            bw.write(serialize(vehicle));
        }
    }

    @Override
    public void addAll(Collection<Vehicle> vehicles) {
        wrapped.addAll(vehicles);
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (var v : vehicles) {
                bw.newLine();
                bw.write(serialize(v));
            }
        }
    }

    @Override
    public boolean remove(Vehicle vehicle) {
        var success = wrapped.remove(vehicle);
        if (success) writeAll();
        return success;
    }

    @Override
    public void clear() {
        wrapped.clear();
        writeAll();
    }

    private void writeAll() {
        try (var fw = new FileWriter(filePath);
             var bw = new BufferedWriter(fw)) {
            bw.write(displayName);
            bw.write('|');
            bw.write(address);
            bw.write('|');
            bw.write(phone);
            for (var v : getAllVehicles()) {
                bw.newLine();
                bw.write(serialize(v));
            }
        }
    }
}
