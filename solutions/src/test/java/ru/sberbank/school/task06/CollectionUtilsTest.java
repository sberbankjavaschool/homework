package ru.sberbank.school.task06;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class CollectionUtilsTest {

    @Test
    void addAllMakeTheirJob() {
        List<Integer> source = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Number> destination = new ArrayList<>(Arrays.asList(4, 5, 6));
        CollectionUtils.addAll(source, destination);
        Assertions.assertEquals(new ArrayList<Number>(Arrays.asList(4, 5, 6, 1, 2, 3)), destination);
    }

    @Test
    void newArrayList() {
        List<Integer> list = CollectionUtils.newArrayList();
        list.add(1);
        Assertions.assertTrue(list.get(0) instanceof Integer);
    }

    @Test
    void indexOfReturnsCorrectIndex() {
        List<Number> list = new ArrayList<>(Arrays.asList(1, 36));
        Assertions.assertEquals(1, CollectionUtils.indexOf(list, 36));
    }

    @Test
    void limitTrimProperlyWhenLimitLessThanSize() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        list = CollectionUtils.limit(list, 2);
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(1, 36)), list);
    }

    @Test
    void limitTrimProperlyWhenZeroLimit() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        list = CollectionUtils.limit(list, 0);
        Assertions.assertEquals(new ArrayList<Integer>(), list);
    }

    @Test
    void limitTrimProperlyWhenLimitMoreThanSize() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        list = CollectionUtils.limit(list, 8);
        Assertions.assertEquals(new ArrayList<>(Arrays.asList(1, 36, 56, 22)), list);
    }

    @Test
    void addAdds() {
        List<Number> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        CollectionUtils.add(list, 3);
        List<Number> expected = new ArrayList<>(Arrays.asList(1, 36, 56, 22, 3));
        Assertions.assertEquals(expected, list);
    }

    @Test
    void removeAll() {
        List<Number> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        List<Integer> listToRemove = new ArrayList<>(Arrays.asList(36, 56));
        CollectionUtils.removeAll(list, listToRemove);
        List<Number> expected = new ArrayList<>(Arrays.asList(1, 22));
        Assertions.assertEquals(expected, list);
    }

    @Test
    void containsAll() {
        List<Number> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        List<Integer> listToSearch = new ArrayList<>(Arrays.asList(36, 56));
        Assertions.assertTrue(CollectionUtils.containsAll(list, listToSearch));
    }

    @Test
    void containsAny() {
        List<Number> list = new ArrayList<>(Arrays.asList(1, 36, 56, 22));
        List<Integer> listToSearch = new ArrayList<>(Arrays.asList(36, 56));
        Assertions.assertTrue(CollectionUtils.containsAll(list, listToSearch));
    }

    @Test
    void range() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 36, 56, 66, 345, 555));
        List<Integer> expected = new ArrayList<>(Arrays.asList(56, 66));
        List<Integer> actual = CollectionUtils.range(list, 50, 70);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void range1() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 36, 56, 66, 345, 555));
        List<Integer> expected = new ArrayList<>(Arrays.asList(56, 66));
        List<Integer> actual = CollectionUtils.range(list, 50, 70, Integer::compareTo);
        Assertions.assertEquals(expected, actual);
    }
}