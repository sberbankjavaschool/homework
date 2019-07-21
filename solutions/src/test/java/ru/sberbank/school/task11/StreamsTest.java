package ru.sberbank.school.task11;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.*;

public class StreamsTest {

    @Test
    public void sort() {
        Set<Integer> res = Streams.of(10,9,8,7,6,5)
                .sorted(Integer::compareTo)
                .toSet();
        Assertions.assertIterableEquals(Arrays.asList(5,6,7,8,9,10), res);
    }

    @Test
    public void toMap() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("!", 1);
        expected.put("hello", 5);
        expected.put("word", 4);

        Map<String, Integer> res = Streams.of("hello","word","!")
                .toMap((item) -> item, String::length);
        Assertions.assertEquals(expected, res);
    }

    @Test
    public void transform() {
        List<Integer> res = Streams.of(10,9,8,7,6,5)
                .transform(i -> i * (-1))
                .toList();
        Assertions.assertIterableEquals(Arrays.asList(-10,-9,-8,-7,-6,-5), res);
    }
}
