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
