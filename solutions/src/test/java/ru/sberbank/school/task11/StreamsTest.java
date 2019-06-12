package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class StreamsTest {

    @Test
    public void testToMap() {
        Map<String, String> expect = new HashMap<>();
        expect.put("pandaKey", "pandaValue");
        expect.put("murKey", "murValue");
        Map<String, String> result = Streams.of(Arrays.asList("panda", "mur"))
                .toMap(v1 -> v1 + "Key", v2 -> v2 + "Value");
        Assertions.assertEquals(expect ,result);
    }

    @Test
    public void testToSet() {
        List<String> setValues = Arrays.asList("mur", "panda");
        Set<String> expect = new HashSet(setValues);
        Set<String> result = Streams.of(setValues).toSet();
        Assertions.assertEquals(expect ,result);
    }

    @Test
    public void testToList() {
        List<Integer> expect = Arrays.asList(1, 2, 3);
        List<Integer> result = Streams.of(expect).toList();
        Assertions.assertEquals(expect ,result);
    }

    @Test
    public void testFilter() {
        List<String> expect = Collections.singletonList(("green"));
        List<String> result = Streams.of(Arrays.asList("red", "green", "blue"))
                .filter(s -> s.equals("green"))
                .toList();
        Assertions.assertEquals(expect ,result);
    }

    @Test
    public void testTransform() {
        List<String> expect = Arrays.asList("YO", "MUR");
        List<String> result = Streams.of(Arrays.asList("yo", "mur"))
                .transform(String::toUpperCase)
                .toList();
        Assertions.assertEquals(expect ,result);
    }

    @Test
    public void sortedTest() {
        List<Integer> expect = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> result = Streams.of(Arrays.asList(5, 4, 3, 2, 1))
                .sorted(Integer::compareTo)
                .toList();
        Assertions.assertEquals(expect ,result);
    }

    @Test
    public void sortedBeforeToSetTest() {
        List<Integer> setValue = Arrays.asList(3, 2, 1);
        Set<Integer> expect = new TreeSet<>(setValue);
        Set<Integer> result = Streams.of(setValue)
                .sorted(Integer::compareTo)
                .toSet();
        Assertions.assertEquals(expect ,result);
    }
}
