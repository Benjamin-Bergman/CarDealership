/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import java.io.*;

@SuppressWarnings("UtilityClass")
final class Program {
    private static final File FILE_PATH = new File("inventory.csv");
    private static final File CONTRACTS_PATH = new File("contracts.yaml");

    public static void main(String[] args) {
        var dealership = new ResourceBackedDealership(
            new BasicDealership("Default_Name", "Default_Address", "Default_Phone"),
            () -> new FileReader(FILE_PATH),
            append -> new FileWriter(FILE_PATH, append));

        var contracts = new FileBackedContractList(CONTRACTS_PATH);

//        ContractListSchema.Entry en = ContractListSchema.Entry
//            .create(LocalDate.now(), "", "", 12345, type.Lease, 100);

        //System.out.println(en.getTotalPrice());

        try (var ui = new DealershipUI(dealership, contracts, System.out, System.in)) {
            ui.display();
        }
    }
}
