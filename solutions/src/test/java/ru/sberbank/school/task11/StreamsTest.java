package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamsTest {

    private List<String> list = Arrays.asList("Java", "Prolog", "Perl", "Ada", "C++");

    @Test
    void ofNullTest() {
        List<String> list = null;
        Assertions.assertThrows(NullPointerException.class, () -> Streams.of(list));
    }

    @Test
    void filterTest() {
        List<Integer> actual = Streams.of(5, 8, 9, 1, 45)
                .filter(x -> x > 8)
                .toList();
        List<Integer> expect = Stream.of(5, 8, 9, 1, 45)
                .filter(x -> x > 8)
                .collect(toList());
        Assertions.assertIterableEquals(expect, actual);
    }

    @Test
    void transformTest() {
        List<String> actual = Streams.of(list)
                .transform(x -> x + "!")
                .toList();
        List<String> expect = list.stream()
                .map(x -> x + "!")
                .collect(toList());
        Assertions.assertIterableEquals(expect, actual);
    }

    @Test
    void filterTransformTest() {
        List<String> actual = Streams.of(list)
                .filter(x -> x.length() == 4)
                .transform(x -> x + " is the best!")
                .toList();
        List<String> expect = list.stream()
                .filter(x -> x.length() == 4)
                .map(x -> x + " is the best!")
                .collect(toList());
        Assertions.assertIterableEquals(expect, actual);
    }

    @Test
    void sortedSetTest() {
        Set<String> actual = Streams.of(list)
                .sorted(Comparator.comparing(String::length))
                .toSet();
        List<String> expect = list.stream()
                .sorted(Comparator.comparing(String::length))
                .distinct()
                .collect(toList());
        Assertions.assertIterableEquals(expect, actual);
    }

    @Test
    void toMapTest() {
        Map<String, Integer> actual = Streams.of(list)
                .filter(x -> x.length() > 3)
                .toMap(Function.identity(), String::length);
        Map<String, Integer> expect = list.stream()
                .filter(x -> x.length() > 3)
                .collect(toMap(Function.identity(), String::length));
        Assertions.assertEquals(expect, actual);
    }

    @Test
    void  toSetTest() {
        Set<String> actual = Streams.of("Java", "Ada", "Perl", "Prolog", "C++", "Java")
                .filter(x -> x.length() > 4)
                .filter(x -> x.length() == 5)
                .toSet();
        Set<String> expect = Stream.of("Java", "Ada", "Perl", "Prolog", "C++", "Java")
                .filter(x -> x.length() > 4)
                .filter(x -> x.length() == 5)
                .collect(toSet());
        Assertions.assertEquals(expect, actual);
    }
}
