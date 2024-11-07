package com.tungnn.tutor.java.core17.base.oop.classes.records;

public class NestedRecordExample {

    record InnerRecord(Integer id, String name) {
        static String fieldStatic = "";
    }
}
