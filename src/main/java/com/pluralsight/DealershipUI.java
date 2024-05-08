/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;

public final class DealershipUI {
    private static final File FILE_PATH = new File("inventory.csv");
    private final Dealership dealership;

    public DealershipUI() {
        dealership = new FileBackedDealership(new Dealership("Default_Name", "Default_Address", "Default_Phone"), FILE_PATH);
    }

    public void display() {
        System.out.println(dealership.getName());
        System.out.println(dealership.getAddress());
        System.out.println(dealership.getPhone());
        dealership.getAllVehicles().stream()
            .map(v -> "%s".formatted(v.model()))
            .forEachOrdered(System.out::println);
    }
}
