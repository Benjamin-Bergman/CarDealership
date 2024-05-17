/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

public final class Contracts {
    private Contracts() {
        throw new InstantiationException("Utility class cannot be instantiated.");
    }

    public static String makeCSV(Contract contract) {
        return "";
    }

    public static Contract fromCSV(String line) {
        return null;
    }
}
