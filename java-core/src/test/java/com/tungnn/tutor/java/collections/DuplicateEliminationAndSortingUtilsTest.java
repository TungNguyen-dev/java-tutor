package com.tungnn.tutor.java.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

public class DuplicateEliminationAndSortingUtilsTest {

  @Test
  public void testEliminateDuplicationAndSort_basic() {
    List<Person> input =
        List.of(
            new Person("1", "Alice", 30),
            new Person("2", "Bob", 25),
            new Person("1", "Alice", 40), // duplicate by key
            new Person("3", "Charlie", 20),
            new Person("4", "Diana", 35), // new additional entry
            new Person("2", "Bob", 25), // exact duplicate of an existing entry
            new Person("5", "Eve", 28) // another new entry
            );

    System.out.println("Input: " + input);

    List<Object> result = DuplicateEliminationAndSortingUtils.eliminateDuplicationAndSort(input);

    System.out.println("Output: " + result);

    assertEquals(5, result.size());
    assertTrue(
        result.stream()
            .anyMatch(p -> ((Person) p).id.equals("1") && ((Person) p).name.equals("Alice")));
    assertTrue(
        result.stream()
            .anyMatch(p -> ((Person) p).id.equals("2") && ((Person) p).name.equals("Bob")));
    assertTrue(
        result.stream()
            .anyMatch(p -> ((Person) p).id.equals("3") && ((Person) p).name.equals("Charlie")));
    assertTrue(
        result.stream()
            .anyMatch(p -> ((Person) p).id.equals("4") && ((Person) p).name.equals("Diana")));
    assertTrue(
        result.stream()
            .anyMatch(p -> ((Person) p).id.equals("5") && ((Person) p).name.equals("Eve")));
  }

  @Test
  public void testEliminateDuplicationAndSort_empty() {
    List<Person> input = Collections.emptyList();

    System.out.println("Input: " + input);

    List<Object> result = DuplicateEliminationAndSortingUtils.eliminateDuplicationAndSort(input);

    System.out.println("Output: " + result);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testEliminateDuplicationAndSort_nullInput() {
    System.out.println("Input: null");

    List<Object> result = DuplicateEliminationAndSortingUtils.eliminateDuplicationAndSort(null);

    System.out.println("Output: " + result);

    assertTrue(result.isEmpty());
  }

  @Test
  public void testEliminateDuplicationAndSort_missingAnnotation() {
    class NoAnnotation {
      final String id;

      NoAnnotation(String id) {
        this.id = id;
      }

      @Override
      public String toString() {
        return id;
      }
    }

    List<NoAnnotation> input = List.of(new NoAnnotation("1"));

    System.out.println("Input: " + input);

    Exception ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              System.out.println("Invoking eliminateDuplicates...");
              DuplicateEliminationAndSortingUtils.eliminateDuplicationAndSort(input);
            });

    System.out.println("Exception: " + ex.getMessage());

    assertTrue(ex.getMessage().contains("annotated with @DuplicateEliminationAndSortingKey"));
  }

  @Test
  public void testEliminateDuplicationAndSort_withNullValues() {
    List<Person> input =
        List.of(
            new Person("1", "Alice", 30),
            new Person(null, "Alice", 30),
            new Person("2", null, 40),
            new Person(null, "Alice", 30) // duplicate of second
            );

    System.out.println("Input: " + input);

    List<Object> result = DuplicateEliminationAndSortingUtils.eliminateDuplicationAndSort(input);

    System.out.println("Output: " + result);

    assertEquals(3, result.size());
  }

  @DuplicateEliminationAndSortingUtils.DuplicateEliminationAndSortingKey(fields = {"id", "name"})
  static class Person {
    String id;
    String name;
    int age;

    Person(String id, String name, int age) {
      this.id = id;
      this.name = name;
      this.age = age;
    }

    @Override
    public String toString() {
      return id + "-" + name + "-" + age;
    }
  }
}
