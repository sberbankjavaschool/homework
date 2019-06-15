package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class StreamsTest {

    @Test
    void ofVarargs() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(5, 7, -4, 6, 55));
        List<Integer> actual = Streams.of(5, 7, -4, 6, 55).toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void ofCollection() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(5, 7, -4, 6, 55));
        List<Integer> actual = Streams.of(expected).toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void filter() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(5, 7, 6, 55));
        List<Integer> actual
                = Streams.of(5, 7, -4, 6, 55)
                .filter(o -> o > 0).toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void transform() {
        List<String> expected = new ArrayList<>(Arrays.asList("5", "7", "6", "55"));
        List<String> actual
                = Streams.of(5, 7, 6, 55)
                .transform(Object::toString).toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void sorted() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(55, 7, 6, 5, -4));
        List<Integer> actual
                = Streams.of(5, 7, -4, 6, 55)
                .sorted(Comparator.reverseOrder())
                .toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toMap() {
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("5", 5);
        expected.put("-4", -4);
        expected.put("66", 66);
        Map<String, Integer> actual
                = Streams.of(5, -4, 66)
                .toMap(Object::toString, o -> o);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toSet() {
        Set<Integer> expected = new LinkedHashSet<>(Arrays.asList(5, 7, -4, 6, 55));
        Set<Integer> actual = Streams.of(5, 7, -4, 6, 55).toSet();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toList() {
        List<Integer> expected = new ArrayList<>(Arrays.asList(5, 7, -4, 6, 55));
        List<Integer> actual = Streams.of(5, 7, -4, 6, 55).toList();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void severalOperations() {
        Map<String, Integer> expected = new LinkedHashMap<>();
        expected.put("7", 7);
        expected.put("6", 6);
        expected.put("5", 5);
        expected.put("-4", -4);
        Map<String, Integer> actual
                = Streams.of(5, 7, -4, 6, 55)
                .filter(o -> o < 50)
                .sorted(Comparator.reverseOrder())
                .transform(Object::toString)
                .toMap(k -> k, Integer::parseInt);

        Assertions.assertEquals(expected, actual);
    }
}