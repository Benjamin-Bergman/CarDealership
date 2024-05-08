/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

/**
 * Represents a vehicle.
 *
 * @param vin         The vehicle's identification number
 * @param year        The vehicle's model year
 * @param make        The vehicle's make
 * @param model       The vehicle's model
 * @param vehicleType The vehicle's type (e.g. {@code "SUV"} or {@code "Truck"})
 * @param color       The vehicle's color
 * @param odometer    The vehicle's odometer reading
 * @param price       The vehicle's price
 */
public record Vehicle(int vin, int year,
                      String make, String model,
                      String vehicleType, String color,
                      int odometer, double price) {
}
