package ru.sberbank.school.task11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class StreamsTest {
    private List<Integer> intList = new ArrayList<>();

    @BeforeEach
    void init() {
        intList.add(1);
        intList.add(2);
        intList.add(3);
        intList.add(4);
        intList.add(5);
        intList.add(6);
        intList.add(7);
        intList.add(8);
        intList.add(9);
        intList.add(10);
    }

    @Test
    @DisplayName("filter method test")
    void filter() {
        Streams<Integer> integerStreams = Streams.of(intList);
        Predicate<Integer> predicate = i -> i > 9;
        Integer result = (Integer) integerStreams.filter(predicate).toList().get(0);
        assertEquals(10, result);
    }

    @Test
    @DisplayName("transform method test")
    void transform() {
        List<String> compareWith = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            compareWith.add("String " + i);
        }
        Streams<Integer> integerStreams = Streams.of(intList);
        Function<Integer, String> function1 = integer -> "String " + integer;

        assertEquals(integerStreams.transform(function1).toList(), compareWith);
    }

    @Test
    @DisplayName("sorting method test")
    void sorted() {
        Collections.reverse(intList);
        Streams<Integer> integerStreams = Streams.of(intList);
        Comparator<Integer> comparator = Comparator.reverseOrder();

        assertEquals(integerStreams.sorted(comparator).toList(), intList);
    }

    @Test
    @DisplayName("toMap method only test")
    void toMap() {
        Streams<Integer> integerStreams = Streams.of(intList);
        Function<Integer, String> function1 = integer -> "String " + integer;
        Function<Integer, Integer> function2 = integer -> integer++;

        assertTrue(integerStreams.toMap(function1, function2) instanceof Map);
    }

    @Test
    @DisplayName("toSet method only test")
    void toSet() {
        Set<Integer> set = new HashSet<>(intList);
        Streams<Integer> integerStreams = Streams.of(intList);

        assertEquals(integerStreams.toSet(), set);
    }

    @Test
    @DisplayName("toList method only test")
    void toListOnly() {
        Streams<Integer> integerStreams = Streams.of(intList);

        assertEquals(intList, integerStreams.toList());
    }

    @Test
    @DisplayName("filter than transform test")
    @SuppressWarnings("unchecked")
    void filterAndTransformList() {
        List<String> list = intList.stream()
                .filter(integer -> integer > 9)
                .map(integer -> "String " + integer)
                .collect(Collectors.toList());

        List<String> integerStreams = Streams.of(intList)
                .filter(integer -> integer > 9)
                .transform(integer -> "String " + integer)
                .toList();

        assertEquals(list, integerStreams);
    }

    @Test
    @DisplayName("filter than map test")
    void filterAndMap() {
        List<String> list = intList.stream()
                .filter(integer -> integer > 9)
                .map(integer -> "String " + integer)
                .collect(Collectors.toList());

        Map<String, Integer> map = new HashMap<>();
        map.put(list.get(0), 10);

        Function<Integer, String> function1 = integer -> "String " + integer;
        Function<Integer, Integer> function2 = integer -> integer;

        Map<String, Integer> result = Streams.of(intList)
                .filter(integer -> integer > 9)
                .toMap(function1, function2);

        assertEquals(map, result);
    }

    @Test
    @DisplayName("filter than list test")
    @SuppressWarnings("unchecked")
    void filterThanList() {
        List<Integer> list = intList.stream()
                .filter(integer -> integer > 9)
                .collect(Collectors.toList());

        List<String> integerStreams = Streams.of(intList)
                .filter(integer -> integer > 9)
                .toList();

        assertEquals(list, integerStreams);
    }

    @Test
    @DisplayName("Transforn than sort test")
    @SuppressWarnings("unchecked")
    void transformThanFilterList() {
        Comparator<Double> comparator = Comparator.reverseOrder();
        List<Double> doubles = intList.stream()
                .map(Integer::doubleValue)
                .sorted(comparator)
                .collect(Collectors.toList());

        List<Double> streamsDoubles = Streams.of(intList)
                .transform(Integer::doubleValue)
                .sorted(comparator)
                .toList();

        assertEquals(doubles, streamsDoubles);
    }
}