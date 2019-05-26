package ru.sberbank.school.task06;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class CountMapImplTest {

    private CountMapImpl<Number> countMap = new CountMapImpl<>();

    @Test
    void addProduceCorrectSizeAndContentMap() {
        countMap.add(1);
        countMap.add(1);
        countMap.add(2);
        Assertions.assertAll(
                () -> Assertions.assertEquals("{1=2, 2=1}", countMap.toString()),
                () -> Assertions.assertEquals(2, countMap.size())
        );
    }

    @Test
    void getCountReturnsRightValueWhenCountNotZero() {
        countMap.add(1);
        countMap.add(1);
        Assertions.assertEquals(2, countMap.getCount(1));
    }

    @Test
    void getCountReturnsRightValueWhenCountZero() {
        Assertions.assertEquals(0, countMap.getCount(1));
    }

    @Test
    void removeReturnsCorrectValueWhenElementWasInMap() {
        countMap.add(1);
        countMap.add(1);
        Assertions.assertEquals(2, countMap.remove(1));
    }

    @Test
    void removeReturnsCorrectValueWhenNoElementInMap() {
        Assertions.assertEquals(0, countMap.remove(1));
    }

    @Test
    void sizeWhenThereIsElements() {
        countMap.add(1);
        countMap.add(2);
        Assertions.assertEquals(2, countMap.size());
    }

    @Test
    void sizeWhenZeroElements() {
        Assertions.assertEquals(0, countMap.size());
    }

    @Test
    void addAllIncrementsAndSumCounters() {
        countMap.add(1);
        CountMapImpl<Integer> anotherCountMap = new CountMapImpl<>();
        anotherCountMap.add(1);
        anotherCountMap.add(2);
        countMap.addAll(anotherCountMap);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, countMap.size()),
                () -> Assertions.assertEquals(2, countMap.getCount(1))
        );
    }

    @Test
    void toMapReturnsProperMap() {
        countMap.add(1);
        countMap.add(1);
        countMap.add(2);
        Map<Number, Integer> expectedMap = new HashMap<>();
        expectedMap.put(1, 2);
        expectedMap.put(2, 1);
        Assertions.assertEquals(expectedMap, countMap.toMap());
    }

    @Test
    void toMapModifyMapProperly() {
        CountMapImpl<Integer> countMap2 = new CountMapImpl<>();
        countMap2.add(1);
        countMap2.add(1);
        countMap2.add(2);
        Map<Number, Integer> expectedMap = new HashMap<>();
        expectedMap.put(1, 2);
        expectedMap.put(2, 1);
        Map<Number, Integer> targetMap = new HashMap<>();
        countMap2.toMap(targetMap);
        Assertions.assertEquals(expectedMap, targetMap);
    }
}