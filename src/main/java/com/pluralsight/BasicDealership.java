/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.util.*;

/**
 * A basic implementation of a {@link Dealership}.
 */
public final class BasicDealership implements Dealership {
    private final String name;
    private final String address;
    private final String phone;
    private final List<Vehicle> inventory = new ArrayList<>();

    /**
     * @param name    This dealership's name
     * @param address This dealership's address
     * @param phone   This dealership's phone number
     */
    public BasicDealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
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
    public String getName() {
        return name;
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(inventory);
    }

    @Override
    public void add(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    @Override
    public void addAll(Collection<Vehicle> vehicles) {
        inventory.addAll(vehicles);
    }

    @Override
    public boolean remove(Vehicle vehicle) {
        return inventory.remove(vehicle);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
