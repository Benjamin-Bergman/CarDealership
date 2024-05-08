/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.util.*;
import java.util.function.*;

/**
 * Represents a car dealership. Ultimately, this is just a collection of {@link Vehicle}s.
 */
public class Dealership {
    private final String name;
    private final String address;
    private final String phone;
    private final List<Vehicle> inventory = new ArrayList<>();

    /**
     * @param name    This dealership's name
     * @param address This dealership's address
     * @param phone   This dealership's phone number
     */
    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    /**
     * @return This dealership's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return This dealership's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return This dealership's name
     */
    public String getName() {
        return name;
    }

    /**
     * @return Every vehicle in this dealership
     */
    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(inventory);
    }

    /**
     * Add a vehicle to this dealership's inventory.
     *
     * @param vehicle The vehicle to add
     */
    public void add(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    /**
     * Adds a collection of vehicles to this dealership's inventory.
     *
     * @param vehicles The vehicles to add
     */
    public void addAll(Collection<Vehicle> vehicles) {
        inventory.addAll(vehicles);
    }

    /**
     * Removes a vehicle from this dealership's inventory
     *
     * @param vehicle The vehicle to remove
     * @return {@code true} if the operation was successful.
     */
    public boolean remove(Vehicle vehicle) {
        return inventory.remove(vehicle);
    }

    /**
     * Removes every vehicle from this dealership's inventory.
     */
    public void clear() {
        inventory.clear();
    }

    /**
     * A collection of common {@link Predicate}s for filtering {@link Vehicle}s.
     */
    @SuppressWarnings("UtilityClass")
    public static final class VehicleFilters {
        private VehicleFilters() {
            throw new InstantiationException("This class cannot be instantiated.");
        }

        /**
         * @param min The minimum price for a vehicle
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> minPrice(double min) {
            return v -> v.price() >= min;
        }

        /**
         * @param max The maximum price for a vehicle
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> maxPrice(double max) {
            return v -> v.price() <= max;
        }

        /**
         * @param make The make of a vehicle, fuzzy searched
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> make(String make) {
            var cleaned = make.trim().toLowerCase();
            return v -> v.make().toLowerCase().contains(cleaned);
        }

        /**
         * @param model The model of a vehicle, fuzzy searched
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> model(String model) {
            var cleaned = model.trim().toLowerCase();
            return v -> v.model().toLowerCase().contains(cleaned);
        }

        /**
         * @param min The minimum year of a vehicle
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> minYear(int min) {
            return v -> v.year() >= min;
        }

        /**
         * @param max The maximum year of a vehicle
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> maxYear(int max) {
            return v -> v.year() <= max;
        }

        /**
         * @param color The color of a vehicle, fuzzy searched
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> color(String color) {
            var cleaned = color.trim().toLowerCase();
            return v -> v.color().toLowerCase().contains(cleaned);
        }

        /**
         * @param min The minimum odometer reading of a vehicle
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> minOdometer(int min) {
            return v -> v.odometer() >= min;
        }

        /**
         * @param max The maximum odometer reading of a vehicle
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> maxOdometer(int max) {
            return v -> v.odometer() <= max;
        }

        /**
         * @param type The type of vehicle, fuzzy searched
         * @return A Predicate applying the condition
         */
        public static Predicate<Vehicle> type(String type) {
            var cleaned = type.trim().toLowerCase();
            return v -> v.vehicleType().toLowerCase().contains(cleaned);
        }
    }
}
