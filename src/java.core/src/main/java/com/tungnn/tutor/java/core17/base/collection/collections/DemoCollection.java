package com.tungnn.tutor.java.core17.base.collection.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class DemoCollection {
    public static void main(String[] args) {
        demoAdd();
        System.out.println();

        demoAddAll();
        System.out.println();

        demoClear();
        System.out.println();

        demoContains();
        System.out.println();

        demoContainsAll();
        System.out.println();

        demoEquals();
        System.out.println();

        demoHashCode();
        System.out.println();

        demoIsEmpty();
        System.out.println();

        demoIterator();
        System.out.println();

        demoRemove();
        System.out.println();

        demoRemoveAll();
        System.out.println();

        demoRemoveIf();
        System.out.println();

        demoRetainAll();
        System.out.println();

        demoSize();
        System.out.println();

        demoToArray();
        System.out.println();
    }

    static void demoAdd() {
        Collection<String> collection = new ArrayList<>();
        printConsole("Collection original", collection);
        printConsole("Add single element", collection.add("A"));
        printConsole("Collection list elements", collection);
    }

    static void demoAddAll() {
        Collection<String> collection = new ArrayList<>(List.of("A"));
        printConsole("Collection original", collection);
        printConsole("Add multi-elements", collection.addAll(List.of("B", "C")));
        printConsole("Collection list elements", collection);
    }

    static void demoClear() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B", "C"));
        printConsole("Collection original", collection);
        collection.clear();
        printConsole("Cleared collection", collection);
    }

    static void demoContains() {
        Collection<String> collection = new ArrayList<>(List.of("A"));
        printConsole("Collection original", collection);
        printConsole("Contains single element 'A'", collection.contains("A"));
        printConsole("Collection list elements", collection);
    }

    static void demoContainsAll() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B"));
        printConsole("Collection original", collection);
        printConsole("Contains multi-element 'A,B'", collection.containsAll(List.of("A", "B")));
        printConsole("Collection list elements", collection);
    }

    static void demoEquals() {
        Collection<String> collection = new ArrayList<>(List.of("apple", "banana", "cherry", "date"));
        Collection<String> sameCollection = List.of("apple", "banana", "cherry", "date");
        printConsole("Collection original", collection);
        printConsole("Compares two collections", collection.equals(sameCollection));
        printConsole("Same collection", sameCollection);
    }

    static void demoHashCode() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B"));
        printConsole("Collection original", collection);
        printConsole("Hash code of collection", collection.hashCode());
    }

    static void demoIsEmpty() {
        Collection<String> collection = new ArrayList<>();
        printConsole("Collection original", collection);
        printConsole("Is collection empty?", collection.isEmpty());
    }

    static void demoIterator() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B", "C"));
        printConsole("Collection original", collection);
        System.out.print("Iterator over collection: ");
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
    }

    static void demoRemove() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B", "C"));
        printConsole("Collection original", collection);
        printConsole("Remove element 'A'", collection.remove("A"));
        printConsole("Collection after removal", collection);
    }

    static void demoRemoveAll() {
        Collection<String> collection = new ArrayList<>(List.of("apple", "banana", "cherry", "date"));
        printConsole("Collection original", collection);
        printConsole("Remove multi-elements 'apple, banana'", collection.removeAll(List.of("apple", "banana")));
        printConsole("Collection after removal", collection);
    }

    static void demoRemoveIf() {
        Collection<String> collection = new ArrayList<>(List.of("apple", "banana", "cherry", "date"));
        printConsole("Collection original", collection);
        printConsole("Remove multi-elements start with 'a'", collection.removeIf(e -> e.startsWith("a")));
        printConsole("Collection after removal", collection);
    }

    static void demoRetainAll() {
        Collection<String> collection = new ArrayList<>(List.of("banana", "cherry", "date"));
        printConsole("Collection original", collection);
        collection.retainAll(List.of("cherry", "date"));
        printConsole("Collection after retainAll", collection);
    }

    static void demoSize() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B", "C"));
        printConsole("Collection original", collection);
        printConsole("Size of collection", collection.size());
    }

    static void demoToArray() {
        Collection<String> collection = new ArrayList<>(List.of("A", "B", "C"));
        printConsole("Collection original", collection);

        // Convert to Object array
        Object[] array = collection.toArray();
        System.out.print("To array (Object[]): ");
        for (Object obj : array) {
            System.out.print(obj + " ");
        }
        System.out.println();

        // Convert to String array
        String[] stringArray = collection.toArray(new String[0]);
        System.out.print("To typed array (String[]): ");
        for (String str : stringArray) {
            System.out.print(str + " ");
        }
        System.out.println();
    }

    static void printConsole(String content, Object result) {
        String msg = String.format("%-40s : %s", content, result);
        System.out.println(msg);
    }
}
