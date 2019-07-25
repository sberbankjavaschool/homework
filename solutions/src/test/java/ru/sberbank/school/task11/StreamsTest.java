package ru.sberbank.school.task11;

import org.junit.jupiter.api.*;

import java.util.*;
import java.util.stream.Collectors;

public class StreamsTest {

    private final static List<String> TEST_VALUES = Arrays.asList("1", "2", "3", "4", "5", "6", "7");

    @Test
    void filter() {
        List<String> list = Streams.of(TEST_VALUES).filter(s -> s.equals("2")).toList();

        Assertions.assertIterableEquals(list, Arrays.asList("2"));
    }

    @Test
    void transform() {
        List<Integer> list = Streams.of(TEST_VALUES).transform(Integer::valueOf).toList();
        List<Integer> list1 = TEST_VALUES.stream().map(Integer::valueOf).collect(Collectors.toList());

        Assertions.assertIterableEquals(list, list1);
    }

    @Test
    void sorted() {
        List<String> list = Streams.of(TEST_VALUES).sorted(String::compareTo).toList();
        List<String> list1 = TEST_VALUES.stream().sorted(String::compareTo).collect(Collectors.toList());

        Assertions.assertIterableEquals(list, list1);
    }

    @Test
    void toMap() {
        Map<String, Integer> map = Streams.of(TEST_VALUES).sorted(String::compareTo).toMap(s -> s, Integer::valueOf);

        Map<String, Integer> map1 = new HashMap<>();
        for (String s : TEST_VALUES) {
            map1.put(s, Integer.valueOf(s));
        }

        Assertions.assertEquals(map, map1);
    }

    @Test
    void toSet() {
        Set<String> set = Streams.of(TEST_VALUES).sorted(String::compareTo).toSet();

        Assertions.assertIterableEquals(set, new LinkedHashSet<>(TEST_VALUES));
    }

    @Test
    void toList() {
        List<String> list = Streams.of(TEST_VALUES).toList();

        Assertions.assertIterableEquals(list, TEST_VALUES);
    }
}
