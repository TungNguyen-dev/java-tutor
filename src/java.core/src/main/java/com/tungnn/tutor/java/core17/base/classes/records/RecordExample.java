package com.tungnn.tutor.java.core17.base.classes.records;

import java.util.Objects;

public record RecordExample(Integer id, String name, Integer age) {

    static String fieldStatic = "Static Field";

    // Compact Constructor
    public RecordExample {
        Objects.requireNonNull(id);
        name += "Mr/Mrs" + "." + name;
    }

    // Canonical Constructor
    public RecordExample() {
        this(null, null, null);
    }

    static String methodStatic() {
        return fieldStatic;
    }

    String methodInstance() {
        return fieldStatic;
    }
}
