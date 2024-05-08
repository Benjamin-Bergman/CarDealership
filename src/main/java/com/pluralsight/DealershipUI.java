/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import com.pluralsight.Dealership.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

/**
 * Represents a user interface for interacting with a {@link Dealership}.
 */
@SuppressWarnings("FeatureEnvy")
public final class DealershipUI implements Closeable {
    private static final File FILE_PATH = new File("inventory.csv");
    @SuppressWarnings("StaticCollection")
    private static final List<String> DISPLAY_OPTIONS = List.of("1", "2", "3", "4", "5", "6", "7");
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

    private Predicate<Vehicle> queryFilterParams(String input) {
        return switch (input) {
            case "1" -> {
                yield VehicleFilters.minPrice(0) & VehicleFilters.maxPrice(Double.POSITIVE_INFINITY);
            }
            case "2" -> {
                yield VehicleFilters.make("") & VehicleFilters.model("");
            }
            case "3" -> {
                yield VehicleFilters.minYear(0) & VehicleFilters.maxYear(Integer.MAX_VALUE);
            }
            case "4" -> {
                yield VehicleFilters.color("");
            }
            case "5" -> {
                yield VehicleFilters.minOdometer(0) & VehicleFilters.maxOdometer(Integer.MAX_VALUE);
            }
            case "6" -> {
                yield VehicleFilters.type("");
            }
            case "7" -> VehicleFilters.all();
            default -> {
                //noinspection ProhibitedExceptionThrown
                throw new RuntimeException("Unreachable");
            }
        };
    }

    private void displayVehicles(Predicate<? super Vehicle> filter) {
        dealership
            .getAllVehicles()
            .stream()
            .filter(filter)
            .forEachOrdered(System.out::println);
    }
}
