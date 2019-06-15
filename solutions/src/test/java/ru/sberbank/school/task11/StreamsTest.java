package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class StreamsTest {
    @Test
    void toMapTest() {
        Map<String, Integer> map = new HashMap<>();
        map.put("first", "first".length());
        map.put("second", "second".length());
        List<String> list = Arrays.asList("first", "second", "first");
        Map<String, Integer> result = Streams.of(list).toMap(k -> k, String::length);

        Assertions.assertEquals(map, result);
    }

    @Test
    void toSetTest() {
        List<String> list = Arrays.asList("first", "second", "first");
        Set<String> set = new HashSet<>(list);
        Set<String> result = Streams.of(list).toSet();

        Assertions.assertEquals(set, result);
    }

    @Test
    void toListTest() {
        String[] strings = {"first", "second"};
        List<String> list = Arrays.asList(strings);
        List<String> result = Streams.of(strings).toList();

        Assertions.assertEquals(list, result);
    }

    @Test
    void filterTest() {
        List<String> list = Arrays.asList("first", "second", "third");
        List<String> expected = Arrays.asList("first", "third");
        List<String> result = Streams.of(list).filter((s) -> s.length() <= 5).toList();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void transformTest() {
        List<String> list = Arrays.asList("first", "second", "third", "fourth");
        List<Integer> expected =  Arrays.asList("first".length(),
                "second".length(), "third".length(), "fourth".length());
        List<Integer> result = Streams.of(list).transform(String::length).toList();

        Assertions.assertEquals(expected, result);
    }

    @Test
    void sortedTest() {
        List<Character> list = Arrays.asList('c', 'd', 'b', 'a');
        List<Character> expected = Arrays.asList('a', 'b', 'c', 'd');
        List<Character> result = Streams.of(list).sorted(Character::compareTo).toList();

        Assertions.assertEquals(expected, result);
    }
}
