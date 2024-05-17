/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import manifold.ext.props.rt.api.*;

/**
 * Represents a contract for a sold/leased {@link Vehicle}.
 */
public interface Contract {
    @val
    double totalPrice;
    @val
    double monthlyPayment;
    @val
    int paymentLength;
    @val
    Vehicle vehicleSold;
}
