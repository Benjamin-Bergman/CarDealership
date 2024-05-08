/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

public record Vehicle(int vin, int year,
                      String make, String model,
                      String vehicleType, String color,
                      int odometer, double price) {
}
