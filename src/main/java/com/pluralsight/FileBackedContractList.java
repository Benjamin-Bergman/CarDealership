/*
 * Copyright (c) Benjamin Bergman 2024.
 */

package com.pluralsight;

import com.pluralsight.ContractListSchema.*;

import java.io.*;
import java.util.*;

/**
 * Represents a {@link ContractCollection} backed by a YAML file.
 */
public class FileBackedContractList implements ContractCollection {
    private final File path;
    private final ContractListSchema list;

    /**
     * @param path The yaml file containing the data.
     */
    public FileBackedContractList(File path) {
        this.path = path;
        list = ContractListSchema.load().fromYamlFile(path);
    }

    @Override
    public Collection<Lease> getLeases() {
        return Collections.unmodifiableList(list.getLeases());
    }

    @Override
    public Collection<Sale> getSales() {
        return Collections.unmodifiableList(list.getSales());
    }

    @Override
    public boolean addLease(Lease lease) {
        list.getLeases().add(lease);
        writeToFile();
        return true;
    }

    @Override
    public boolean addSale(Sale sale) {
        list.getSales().add(sale);
        writeToFile();
        return true;
    }

    private void writeToFile() {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(list.write().toYaml());
        }
    }
}
