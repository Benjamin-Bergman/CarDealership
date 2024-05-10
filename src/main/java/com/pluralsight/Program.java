/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;

@SuppressWarnings("UtilityClass")
final class Program {
    private static final File FILE_PATH = new File("inventory.csv");

    public static void main(String[] args) {
        var dealership = new FileBackedDealership(new BasicDealership("Default_Name", "Default_Address", "Default_Phone"), FILE_PATH);

        try (var ui = new DealershipUI(dealership, System.out, System.in)) {
            ui.display();
        }
    }
}
