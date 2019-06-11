package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class MyStreamsTest {

    @Test
    @DisplayName("Тест на выброс NP при передаче null коллекции")
    void of() {
        List<String> list = null;
        Assertions.assertThrows(NullPointerException.class, () -> MyStreams.of(list));
    }

    @Test
    void toSet() {
        Set<String> strings = MyStreams.of("apple", "banana", "orange", "lemon", "strawberry", "lemon", "strawberry").toSet();
        Set<String> set = Stream.of("apple", "banana", "orange", "lemon", "strawberry", "lemon", "strawberry").collect(Collectors.toSet());

        Assertions.assertIterableEquals(set, strings);
    }

    @Test
    void toList() {
        List<String> list = MyStreams.of("apple", "banana", "orange", "lemon", "strawberry").toList();

        Assertions.assertIterableEquals(Arrays.asList("apple", "banana", "orange", "lemon", "strawberry"), list);
    }

    @Test
    void filter() {
        List<String> list = MyStreams.of("apple", "banana", "orange", "lemon", "strawberry").filter(s -> s.length() == 5).toList();

        Assertions.assertIterableEquals(Arrays.asList("apple", "lemon"), list);
    }

    @Test
    void transform() {
        List<Integer> integerList = MyStreams.of("apple", "banana", "orange", "lemon", "strawberry", "lemon", "strawberry").transform(String::length).toList();
        List<Integer> integerList1 = Stream.of("apple", "banana", "orange", "lemon", "strawberry", "lemon", "strawberry").map(String::length).collect(Collectors.toList());

        Assertions.assertIterableEquals(integerList, integerList1);
    }

    @Test
    void sorted() {
        List<String> list = MyStreams.of("lemon", "strawberry", "apple", "banana", "orange", "lemon", "strawberry").sorted(String::compareTo).toList();
        List<String> list1 = Stream.of("lemon", "strawberry", "apple", "banana", "orange", "lemon", "strawberry").sorted(String::compareTo).collect(Collectors.toList());

        Assertions.assertIterableEquals(list, list1);
    }

    @Test
    void toMap() {
        List<String> list = Arrays.asList("lemon", "strawberry", "apple", "banana", "orange");
        Map<String, Integer> stringIntegerMap = MyStreams.of(list).sorted(String::compareTo).toMap(s -> s, String::length);

        Map<String, Integer> stringIntegerMap1 = new HashMap<>();
        for (String s : list) {
            stringIntegerMap1.put(s, s.length());
        }

        Assertions.assertEquals(stringIntegerMap1, stringIntegerMap);
    }

}