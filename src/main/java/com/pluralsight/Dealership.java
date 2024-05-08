/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.util.*;

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
}
