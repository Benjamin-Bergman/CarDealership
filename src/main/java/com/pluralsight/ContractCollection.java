/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import com.pluralsight.ContractListSchema.*;

import java.util.*;

/**
 * Represents a mutable collection of {@link Entry}s.
 */
public interface ContractCollection {
    static double getTotalPrice(ContractListSchema.Entry entry) {
        return switch (entry.getType()) {
            case Lease -> entry.getBasePrice() * 1.07;
            case Sale, SaleFinanced -> entry.getBasePrice() * 1.05 + 100 + (entry.getBasePrice() < 10_000 ? 295 : 495);
        };
    }

    /**
     * @return An unmodifiable view of leases
     */
    Collection<Entry> getLeases();

    /**
     * @return An unmodifiable view of sales
     */
    Collection<Entry> getSales();

    /**
     * @param entry An entry to add
     * @return {@code true} on success
     */
    boolean add(Entry entry);
}
