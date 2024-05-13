/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import com.pluralsight.ContractListSchema.*;

import java.util.*;

/**
 * Represents a mutable collection of {@link Sale}s and {@link Lease}s.
 */
public interface ContractCollection {
    /**
     * @return An unmodifiable view of leases
     */
    Collection<Lease> getLeases();

    /**
     * @return An unmodifiable view of sales
     */
    Collection<Sale> getSales();

    /**
     * @param lease A lease to add
     * @return {@code true} on success
     */
    boolean addLease(Lease lease);

    /**
     * @param sale A sale to add
     * @return {@code true} on success
     */
    boolean addSale(Sale sale);
}
