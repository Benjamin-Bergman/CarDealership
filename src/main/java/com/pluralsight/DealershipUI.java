/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;

/**
 * Represents a user interface for interacting with a {@link Dealership}.
 */
@SuppressWarnings("FeatureEnvy")
public final class DealershipUI implements Closeable {
    private static final File FILE_PATH = new File("inventory.csv");
    @SuppressWarnings("StaticCollection")
    private static final List<String> DISPLAY_OPTIONS = List.of("1", "2", "3", "4", "5", "6", "7");
    private static final Pattern MONEY_PATTERN = Pattern.compile("^\\$?(?!0*\\.00?$)(\\d*(?:\\.\\d\\d?)?)$");
    private static final Pattern INT_PATTERN = Pattern.compile("^\\d+$");
    private final Dealership dealership;
    private final Scanner scanner;

    /**
     * Creates a new instance of the UI.
     */
    public DealershipUI() {
        dealership = new FileBackedDealership(new Dealership("Default_Name", "Default_Address", "Default_Phone"), FILE_PATH);
        scanner = new Scanner(System.in);
    }

    /**
     * Runs the UI.
     */
    public void display() {
        System.out.println("Welcome to " + dealership.getName() + '!');
        loop:
        while (true) {
            System.out.print("""
                --SEARCH--
                1 - By price
                2 - By make/model
                3 - By year
                4 - By color
                5 - By odometer
                6 - By type
                7 - Show all
                --OTHER--
                8 - Add vehicle
                9 - Remove vehicle
                99 - Exit
                Choose an option:\s""");
            var input = scanner.nextLine().trim();
            if (DISPLAY_OPTIONS.contains(input)) {
                displayVehicles(queryFilterParams(input));
                readKey();
                continue;
            }

            switch (input) {
                case "8" -> {
                }
                case "9" -> {
                }
                case "99" -> {
                    break loop;
                }
                default -> System.out.println("Unknown option $input! Please try again.");
            }
        }

        System.out.println("Thanks for stopping by!");
    }

    @Override
    public void close() {
        scanner.close();
    }

    private void readKey() {
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    private Predicate<Vehicle> queryFilterParams(String input) {
        return switch (input) {
            case "1" -> {
                var min = queryMoneyValue("minimum");
                var max = queryMoneyValue("maximum");
                yield VehicleFilters.minPrice(min) & VehicleFilters.maxPrice(max);
            }
            case "2" -> {
                var make = queryStringValue("make");
                var model = queryStringValue("model");
                yield VehicleFilters.make(make) & VehicleFilters.model(model);
            }
            case "3" -> {
                var min = queryIntValue("minimum year");
                var max = queryIntValue("maximum year");
                yield VehicleFilters.minYear(min) & VehicleFilters.maxYear(max);
            }
            case "4" -> VehicleFilters.color(queryStringValue("color"));
            case "5" -> {
                var min = queryIntValue("minimum reading");
                var max = queryIntValue("maximum reading");
                yield VehicleFilters.minOdometer(min) & VehicleFilters.maxOdometer(max);
            }
            case "6" -> VehicleFilters.type(queryStringValue("type"));
            case "7" -> VehicleFilters.all();
            default -> //noinspection ProhibitedExceptionThrown
                throw new RuntimeException("Unreachable");
        };
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    private int queryIntValue(String which) {
        while (true) {
            System.out.print("Enter the $which: ");
            var input = scanner.nextLine().trim();
            var match = INT_PATTERN.matcher(input);
            if (match.matches()) try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ignored) {
                // Reachable for an input like 9999999999999999999
            }
            System.out.println("Bad input, please try again.");
        }
    }

    private String queryStringValue(String which) {
        System.out.print("Enter the $which: ");
        return scanner.nextLine().trim();
    }

    @SuppressWarnings("ObjectAllocationInLoop")
    private double queryMoneyValue(String which) {
        while (true) {
            System.out.print("Enter the $which price: ");
            var input = scanner.nextLine().trim();
            var match = MONEY_PATTERN.matcher(input);
            if (match.matches()) try {
                return Double.parseDouble(match.group(1));
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Bad input, please try again.");
        }
    }

    private void displayVehicles(Predicate<? super Vehicle> filter) {
        dealership
            .getAllVehicles()
            .stream()
            .filter(filter)
            .forEachOrdered(System.out::println);
    }
}
