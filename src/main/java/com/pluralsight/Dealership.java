/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.util.*;

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
}
