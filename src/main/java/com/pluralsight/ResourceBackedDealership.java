/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Decorates a {@link Dealership} with file-saving semantics.
 */
public final class ResourceBackedDealership implements Dealership {
    private final Function<Boolean, Writer> writer;
    private final Dealership wrapped;
    private final String displayName, address, phone;

    /**
     * Creates a new ResourceBackedDealership.
     * Note that this replaces the contents of {@code wrapped} with the contents of the resource.
     *
     * @param wrapped The Dealership to decorate
     * @param reader  A supplier to create a readable stream of the resource.
     * @param writer  A supplier to create a writable stream of the resource.
     *                It accepts one boolean argument: if {@code true},
     *                the writer should implement appending semantics,
     *                otherwise it should implement overwriting semantics.
     */
    public ResourceBackedDealership(Dealership wrapped, Supplier<Reader> reader, Function<Boolean, Writer> writer) {
        this.wrapped = wrapped;
        this.writer = writer;

        wrapped.clear();
        boolean shouldRewrite;

        try (var fr = reader.get();
             var br = new BufferedReader(fr)) {
            var header = br.readLine();

            String[] parts;
            //noinspection NestedAssignment
            if (header != null && (parts = header.split("\\|")).length == 3) {
                displayName = parts[0];
                address = parts[1];
                phone = parts[2];
                shouldRewrite = false;
            } else if (
                isValid(wrapped.getDisplayName())
                && isValid(wrapped.getAddress())
                && isValid(wrapped.getPhone())
            ) {
                displayName = wrapped.getDisplayName();
                address = wrapped.getAddress();
                phone = wrapped.getPhone();
                shouldRewrite = true;
            } else throw new IOException("Bad file header when reading");


            wrapped.addAll(
                br.lines()
                    .map(ResourceBackedDealership::parseLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
            );
        }

        if (shouldRewrite)
            writeAll();
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
        try (var fw = writer.apply(true);
             var bw = new BufferedWriter(fw)) {
            bw.newLine();
            bw.write(serialize(vehicle));
        }
    }

    @Override
    public void addAll(Collection<Vehicle> vehicles) {
        wrapped.addAll(vehicles);
        try (var fw = writer.apply(true);
             var bw = new BufferedWriter(fw)) {
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
        try (var fw = writer.apply(false);
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