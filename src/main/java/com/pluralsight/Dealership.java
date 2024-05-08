/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.util.*;

public class Dealership {
    private final String name, address, phone;

    private final List<Vehicle> inventory = new ArrayList<>();

    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(inventory);
    }

    public void add(Vehicle v) {
        inventory.add(Objects.requireNonNull(v, "Added vehicle cannot be null."));
    }

    public boolean remove(Vehicle v) {
        return inventory.remove(Objects.requireNonNull(v, "Removed vehicle cannot be null."));
    }
}
