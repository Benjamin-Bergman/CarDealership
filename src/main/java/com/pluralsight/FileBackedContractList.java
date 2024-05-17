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
    public Collection<Entry> getLeases() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public Collection<Entry> getSales() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public boolean add(Entry entry) {
        list.add(entry);
        writeToFile();
        return true;
    }

    private void writeToFile() {
        try (FileWriter fw = new FileWriter(path)) {
            fw.write(list.write().toYaml());
        }
    }
}