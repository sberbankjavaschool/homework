package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class StreamsImplTest {

    @Test
    void testCreationFromVararg() {
        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three");

        Assertions.assertEquals("StreamsImpl{items=[one, two, three]}", stream.toString());
    }

    @Test
    void testCreationFromCollection() {
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add("one");
        hashSet.add("two");
        hashSet.add("three");
        StreamsImpl<String> stream = StreamsImpl.of(hashSet);

        Assertions.assertEquals("StreamsImpl{items=[one, two, three]}", stream.toString());
    }

    @Test
    void testFilterStringShorterThanFour() {
        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three").filter(s -> s.length() < 4);

        Assertions.assertEquals("StreamsImpl{items=[one, two]}", stream.toString());
    }

    @Test
    void testTransformStringToIntOfLength() {
        StreamsImpl<Integer> expectedStream = StreamsImpl.of(3, 3, 5);
        StreamsImpl<Integer> stream = StreamsImpl.of("one", "two", "three").transform(String::length);

        Assertions.assertEquals(expectedStream, stream);
    }

    @Test
    void testSortedStringsWithDefaultSorter() {
        StreamsImpl<String> expectedStream = StreamsImpl.of("one", "three", "two");
        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three").sorted(String::compareTo);

        Assertions.assertEquals(expectedStream, stream);
    }

    @Test
    void testStreamOfStringsToMapOfHashsetAndString() {
        Map<Integer, String> expectedMap = new HashMap<>();
        expectedMap.put("one".hashCode(), "one");
        expectedMap.put("two".hashCode(), "two");
        expectedMap.put("three".hashCode(), "three");

        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three");
        Map<Integer, String> actualMap = stream.toMap(String::hashCode, s -> s);

        Assertions.assertEquals(expectedMap, actualMap);
    }

    @Test
    void testToSetOfStringsDoingFine() {
        Set<String> expected = new HashSet<>(Arrays.asList("one", "two", "three"));
        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three", "one", "two", "three", "two", "three");

        Assertions.assertEquals(expected, stream.toSet());
    }

    @Test
    void testToListOfStringsDoingFine() {
        List<String> expected = Arrays.asList("one", "two", "three");
        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three");

        Assertions.assertEquals(expected, stream.toList());
    }

    @Test
    void chainingTestFromListOfMapEntires() {
        List<Map.Entry<String, Integer>> list = new LinkedList<>();
        list.add(new AbstractMap.SimpleEntry<>("Persone One", 40));
        list.add(new AbstractMap.SimpleEntry<>("Persone Two", 20));
        list.add(new AbstractMap.SimpleEntry<>("Persone Three", 10));
        list.add(new AbstractMap.SimpleEntry<>("Persone Four", 3));
        list.add(new AbstractMap.SimpleEntry<>("Persone Five", 99));

        Map<String, Map.Entry<String, Integer>> expected = new HashMap<>();
        expected.put("Persone Two", new AbstractMap.SimpleEntry<>("Persone Two", 50));
        expected.put("Persone One", new AbstractMap.SimpleEntry<>("Persone One", 70));
        expected.put("Persone Five", new AbstractMap.SimpleEntry<>("Persone Five", 129));

        Map<String, Map.Entry<String, Integer>> map = StreamsImpl.of(list)
                .filter(p -> p.getValue() >= 20)
                .transform(p -> new AbstractMap.SimpleEntry<>(p.getKey(), p.getValue() + 30))
                .sorted(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
                .toMap(AbstractMap.SimpleEntry::getKey, p -> p);

        Assertions.assertEquals(expected, map);
    }

    @Test
    void chainSortedAndToSetReturnsSortedSet() {
        Set<String> expected = new HashSet<>(Arrays.asList("one", "three", "two"));
        StreamsImpl<String> stream = StreamsImpl.of("one", "two", "three", "one", "two", "three", "two", "three");

        Assertions.assertIterableEquals(expected, stream.sorted(String::compareTo).toSet());
    }

    @Test
    void chainSortedAndToListReturnsSortedList() {
        List<String> expected = new LinkedList<>(Arrays
                .asList("one", "one", "three", "three", "three", "two", "two", "two"));
        StreamsImpl<String> stream = StreamsImpl
                .of("one", "two", "three", "one", "two", "three", "two", "three");

        Assertions.assertIterableEquals(expected, stream.sorted(String::compareTo).toList());
    }
}