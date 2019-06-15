package ru.sberbank.school.task11;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.*;

public class StreamsImplTest {
    @Test
    public void testFilter() {
        List<Integer> list = StreamsImpl.of(1, 9, 5, 7, 3, 7565, 32, 54332, 1)
                .filter((x) -> x > 30)
                .toList();
        List<Integer> actual = Arrays.asList(7565, 32, 54332);
        Assertions.assertIterableEquals(list, actual);
    }

    @Test
    public void testSorted() {
        Set<Integer> actual = new LinkedHashSet<>();
        actual.add(0);
        actual.add(1);
        actual.add(2);
        actual.add(32);
        actual.add(52);
        actual.add(12345);

        Set<Integer> set = StreamsImpl.of(Arrays.asList(1, 12345, 2, 32, 52, 0))
                .sorted(Integer::compareTo)
                .toSet();

        Assertions.assertIterableEquals(set, actual);
    }

    @Test
    public void testTransform() {
        List<Double> list = StreamsImpl.of(5, 43, 6, 21, 43, 1)
                .transform(Integer::doubleValue)
                .toList();

        List<Double> actual = Arrays.asList(5.0, 43.0, 6.0, 21.0, 43.0, 1.0);

        Assertions.assertIterableEquals(list, actual);
    }

    @Test
    public void testToMap() {
        Map<String, String> actual = new HashMap<>();
        actual.put("one key", "one value");
        actual.put("two key", "two value");
        actual.put("three key", "three value");

        Map<String, String> map = StreamsImpl.of("one", "two", "three")
                .toMap((elem) -> elem + " key", (elem) -> elem + " value");

        Assert.assertThat(map, CoreMatchers.is(actual));
    }
}
