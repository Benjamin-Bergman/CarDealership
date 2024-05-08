/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

@SuppressWarnings("UtilityClass")
final class Program {
    public static void main(String[] args) {
        try (var ui = new DealershipUI()) {
            ui.display();
        }
    }
}
