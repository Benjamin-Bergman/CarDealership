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
    @SuppressWarnings("StaticCollection")
    private static final List<String> DISPLAY_OPTIONS = List.of("1", "2", "3", "4", "5", "6", "7");
    private static final Pattern MONEY_PATTERN = Pattern.compile("^\\$?(?!0*\\.00?$)(\\d*(?:\\.\\d\\d?)?)$");
    private static final Predicate<String> INT_PATTERN = Pattern.compile("^\\d+$").asPredicate();
    private final Dealership dealership;
    private final Scanner scanner;

    /**
     * Creates a new instance of the UI.
     *
     * @param dealership The dealership this UI controls
     */
    public DealershipUI(Dealership dealership) {
        this.dealership = dealership;
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
                case "8" -> addVehicle();
                case "9" -> removeVehicle();
                case "99" -> {
                    break loop;
                }
                default -> System.out.println("Unknown option \"$input\"! Please try again.");
            }
        }

        System.out.println("Thanks for stopping by!");
    }

    @Override
    public void close() {
        scanner.close();
    }

    @SuppressWarnings({"ReassignedVariable", "MethodWithMoreThanThreeNegations",
        "MethodWithMultipleLoops", "OverlyComplexMethod", "OverlyLongMethod"})
    private void removeVehicle() {
        var price = queryMoneyValue("vehicle's", -1.0);
        var make = queryStringValue("make", true);
        var model = queryStringValue("model", true);
        var year = queryIntValue("year", -1);
        var color = queryStringValue("color", true);
        var odometer = queryIntValue("odometer reading", -1);
        var type = queryStringValue("type", true);
        var vin = queryIntValue("VIN", -1);

        var filter =
            VehicleFilters.make(make)
            & VehicleFilters.model(model)
            & VehicleFilters.color(color)
            & VehicleFilters.type(type);
        //noinspection FloatingPointEquality
        if (price != -1.0)
            filter &= VehicleFilters.maxPrice(price) & VehicleFilters.minPrice(price);
        if (year != -1)
            filter &= VehicleFilters.maxYear(year) & VehicleFilters.minYear(year);
        if (odometer != -1)
            filter &= VehicleFilters.maxOdometer(odometer) & VehicleFilters.minOdometer(odometer);
        if (vin != -1)
            filter &= VehicleFilters.vin(vin);

        var found = dealership
            .getAllVehicles()
            .stream()
            .filter(filter)
            .toList();

        if (found.isEmpty()) {
            System.out.println("Found no matching vehicles. Aborting...");
            readKey();
            return;
        }

        //noinspection HardcodedFileSeparator
        System.out.print(
            found.size() == 1 ?
                """
                    Found one matching vehicle:
                    ${found[0]}
                    Remove it? [y/n]"""
                : "Found ${found.size()} matching vehicles. Remove all of them? [y/n]"
        );

        boolean shouldDelete;
        while (true) {
            var input = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(input) || "n".equals(input)
                || "yes".equals(input) || "no".equals(input)) {
                shouldDelete = "y".equals(input) || "yes".equals(input);
                break;
            }
            //noinspection HardcodedFileSeparator
            System.out.print("Unknown option \"$input\". Try again: [y/n] ");
        }
        if (shouldDelete) {
            for (var v : found)
                dealership.remove(v);
            System.out.println("Removed ${found.size()} vehicles.");
        } else
            System.out.println("Nothing removed.");

        readKey();
    }

    private void addVehicle() {
        var price = queryMoneyValue("vehicle's", null);
        var make = queryStringValue("make", false);
        var model = queryStringValue("model", false);
        var year = queryIntValue("year", null);
        var color = queryStringValue("color", false);
        var odometer = queryIntValue("odometer reading", null);
        var type = queryStringValue("type", false);
        var vin = queryIntValue("VIN", null);

        var v = new Vehicle(vin, year, make, model, type, color, odometer, price);
        dealership.add(v);
        System.out.print("""
            Successfully added the vehicle:
            $v
            """);
        readKey();
    }

    private void readKey() {
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

    private Predicate<Vehicle> queryFilterParams(String input) {
        return switch (input) {
            case "1" -> {
                var min = queryMoneyValue("minimum", Double.NEGATIVE_INFINITY);
                var max = queryMoneyValue("maximum", Double.POSITIVE_INFINITY);
                yield VehicleFilters.minPrice(min) & VehicleFilters.maxPrice(max);
            }
            case "2" -> {
                var make = queryStringValue("make", true);
                var model = queryStringValue("model", true);
                yield VehicleFilters.make(make) & VehicleFilters.model(model);
            }
            case "3" -> {
                var min = queryIntValue("minimum year", Integer.MIN_VALUE);
                var max = queryIntValue("maximum year", Integer.MAX_VALUE);
                yield VehicleFilters.minYear(min) & VehicleFilters.maxYear(max);
            }
            case "4" -> VehicleFilters.color(queryStringValue("color", true));
            case "5" -> {
                var min = queryIntValue("minimum reading", Integer.MIN_VALUE);
                var max = queryIntValue("maximum reading", Integer.MAX_VALUE);
                yield VehicleFilters.minOdometer(min) & VehicleFilters.maxOdometer(max);
            }
            case "6" -> VehicleFilters.type(queryStringValue("type", true));
            case "7" -> VehicleFilters.all();
            default -> //noinspection ProhibitedExceptionThrown
                throw new RuntimeException("Unreachable");
        };
    }

    private int queryIntValue(String which, Integer defaultValue) {
        while (true) {
            System.out.print("Enter the $which: ");
            var input = scanner.nextLine().trim();
            if (defaultValue != null && input.isEmpty())
                return defaultValue;
            if (INT_PATTERN.test(input)) try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ignored) {
                // Reachable for an input like 9999999999999999999
            }
            System.out.println("Bad input, please try again.");
        }
    }

    private String queryStringValue(String which, boolean allowEmpty) {
        while (true) {
            System.out.print("Enter the $which: ");
            var input = scanner.nextLine().trim();
            if (allowEmpty || !input.isEmpty())
                return input;
            System.out.println("Bad input, please try again.");
        }
    }

    private double queryMoneyValue(String which, Double defaultValue) {
        while (true) {
            System.out.print("Enter the $which price: ");
            var input = scanner.nextLine().trim();
            if (defaultValue != null && input.isEmpty())
                return defaultValue;
            //noinspection ObjectAllocationInLoop
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
