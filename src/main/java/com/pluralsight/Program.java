/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;

@SuppressWarnings("UtilityClass")
final class Program {
    private static final File FILE_PATH = new File("inventory.csv");

    public static void main(String[] args) {
        var dealership = new ResourceBackedDealership(
            new BasicDealership("Default_Name", "Default_Address", "Default_Phone"),
            () -> new FileReader(FILE_PATH),
            append -> new FileWriter(FILE_PATH, append));

        try (var ui = new DealershipUI(dealership, System.out, System.in)) {
            ui.display();
        }
    }
}
