package ru.sberbank.school.task12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

class StreamsTest {

    private final String[] source = {"oak", "maple", "sakura", "palm", "spruce", "pine", "baobab", "sakura"};

    @Test
    void filterTest() {
        List<String> target = Arrays.asList("spruce", "pine");
        List<String> result = Streams.of(source)
                .filter(s -> !s.matches(".+[a]+.+"))
                .toList();
        Assertions.assertIterableEquals(target, result);
        System.out.println("filterTest result: " + result.toString());
    }

    @Test
    void transformTest() {
        List<String> target = Arrays.asList("oak-tree", "maple-tree", "sakura-tree", "palm-tree",
                                            "spruce-tree", "pine-tree", "baobab-tree", "sakura-tree");
        List<String> result = Streams.of(source)
                .transform(e -> e += "-tree")
                .toList();
        Assertions.assertIterableEquals(target, result);
        System.out.println("transformTest result: " + result.toString());
    }

    @Test
    void sortedTest() {
        List<String> target = Arrays.asList("baobab", "maple", "oak", "palm", "pine", "sakura", "sakura", "spruce");
        List<String> result = Streams.of(source)
                .sorted(String::compareTo)
                .toList();
        Assertions.assertIterableEquals(target, result);
        System.out.println("sortedTest result: " + result.toString());
    }

    @Test
    void toMapTest() {
        Map<String, Integer> target = new HashMap<>();
        for (String s : source) {
            target.put(s += "-tree", s.length());
        }
        Map<String, Integer> result = Streams.of(source)
                .toMap(s -> s += "-tree", s -> s.length() + 5);
        Assertions.assertEquals(target, result);
        System.out.println("toMapTest result: " + result.toString());
    }

    @Test
    void toListTest() {
        List<String> target = Arrays.asList(source);
        List<String> result = Streams.of(source).toList();
        Assertions.assertIterableEquals(target, result);
        System.out.println("toListTest result: " + result.toString());
    }

    @Test
    void toSetTest() {
        Set<String> target = new TreeSet<>(String::compareTo);
        target.addAll(Arrays.asList(source));

        Set<String> result = Streams.of(source)
                .sorted(String::compareTo)
                .toSet();
        Assertions.assertIterableEquals(target, result);
        System.out.println("toSetTest result: " + result.toString());
    }

    @Test
    void ofNullTest() {
        List<String> list = null;
        Assertions.assertThrows(NullPointerException.class, () -> Streams.of(list));
    }

    @Test
    void fluentTest() {
        Set<String> target = new LinkedHashSet<>(Arrays.asList("BAOBAB", "MAPLE", "OAK", "PALM", "SAKURA"));
        Set<String> result = Streams.of(source)
                .filter(s -> s.matches(".+[a]+.+"))
                .transform(String::toUpperCase)
                .sorted(String::compareTo)
                .toSet();
        Assertions.assertIterableEquals(target, result);
        System.out.println("fluentTest result: " + result.toString());
    }
}