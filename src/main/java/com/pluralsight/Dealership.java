/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.util.*;
import java.util.function.*;

public class Dealership {
    private final String name;
    private final String address;
    private final String phone;
    private final List<Vehicle> inventory = new ArrayList<>();

    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(inventory);
    }

    public void add(Vehicle v) {
        inventory.add(v);
    }

    public void addAll(Collection<Vehicle> vs) {
        inventory.addAll(vs);
    }

    public boolean remove(Vehicle v) {
        return inventory.remove(v);
    }

    public void clear() {
        inventory.clear();
    }

    public static final class Filter {
        private Filter() {
            throw new InstantiationException("This class cannot be instantiated.");
        }

        public static Predicate<Vehicle> minPrice(double min) {
            return v -> v.price() >= min;
        }

        public static Predicate<Vehicle> maxPrice(double max) {
            return v -> v.price() <= max;
        }

        public static Predicate<Vehicle> make(String make) {
            var cleaned = make.trim().toLowerCase();
            return v -> v.make().toLowerCase().contains(cleaned);
        }

        public static Predicate<Vehicle> model(String model) {
            var cleaned = model.trim().toLowerCase();
            return v -> v.model().toLowerCase().contains(cleaned);
        }

        public static Predicate<Vehicle> minYear(int min) {
            return v -> v.year() >= min;
        }

        public static Predicate<Vehicle> maxYear(int max) {
            return v -> v.year() <= max;
        }

        public static Predicate<Vehicle> color(String color) {
            var cleaned = color.trim().toLowerCase();
            return v -> v.color().toLowerCase().contains(cleaned);
        }

        public static Predicate<Vehicle> minOdometer(int min) {
            return v -> v.odometer() >= min;
        }

        public static Predicate<Vehicle> maxOdometer(int max) {
            return v -> v.odometer() <= max;
        }

        public static Predicate<Vehicle> type(String type) {
            var cleaned = type.trim().toLowerCase();
            return v -> v.vehicleType().toLowerCase().contains(cleaned);
        }
    }
}
