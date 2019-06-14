package ru.sberbank.school.task11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StreamsImplTest {

    @Test
    void checkThrowNonNullException() {
        List<Integer> intList = null;
        Assertions.assertThrows(NullPointerException.class, () -> StreamsImpl.of(intList)
                .toList());
    }

    @Test
    void filter() {
        List<String> stringList = Arrays.asList("Washington Capitals", "Pittsburg Pens", "STLuise Blues",
                "Edmonton Oilers", "Boston Bruins", "Carolina Hurricanes", "Toronto Maple Leafes");
        List<String> filteredList = StreamsImpl.of(stringList)
                .filter(s -> s.length() < 17)
                .toList();
        List<String> equalList = Arrays.asList("Pittsburg Pens", "STLuise Blues",
                "Edmonton Oilers", "Boston Bruins");
        Assertions.assertIterableEquals(filteredList, equalList);
    }

    @Test
    void transform() {
        List<String> stringList = Arrays.asList("Washington Capitals", "Pittsburg Pens", "STLuise Blues",
                "Edmonton Oilers", "Boston Bruins", "Carolina Hurricanes", "Toronto Maple Leafes");
        List<String> transformedList = StreamsImpl.of(stringList)
                .transform(s -> s + " NHL")
                .toList();
        List<String> equalList = Arrays.asList("Washington Capitals NHL", "Pittsburg Pens NHL", "STLuise Blues NHL",
                "Edmonton Oilers NHL", "Boston Bruins NHL", "Carolina Hurricanes NHL", "Toronto Maple Leafes NHL");
        //System.out.println(filteredList);
        Assertions.assertIterableEquals(transformedList, equalList);
    }

    @Test
    void sorted() {
        int[] intArray = null;
        List<Integer> intList = Arrays.asList(6, 1, 7, 2, 8, 3, 3, 4, 9, 8, 2, 5, 7, 10);
        List<Integer> sortedList = StreamsImpl.of(intList)
                .sorted(Comparator.naturalOrder())
                .toList();
        List<Integer> equalList = Arrays.asList(1, 2, 2, 3, 3, 4, 5, 6, 7, 7, 8, 8, 9, 10);
        Assertions.assertIterableEquals(sortedList, equalList);
    }

    @Test
    void toMap() {
        int[] intArray = null;
        List<Integer> intList = Arrays.asList(6, 1, 7, 2, 8, 3, 3, 4, 9, 8, 2, 5, 7, 10);
        Map<Integer, String> sortedMap = StreamsImpl.of(intList)
                .sorted(Comparator.naturalOrder())
                .toMap(integer -> integer, integer -> " String int " + integer.toString());
        Map<Integer, String> equalMap = new HashMap<Integer, String>();
        for (int i = 0; i < intList.size(); i++) {
            equalMap.put(intList.get(i), " String int " + intList.get(i));
        }
        Assertions.assertEquals(sortedMap, equalMap);
    }

    @Test
    void toSet() {
        int[] intArray = null;
        List<Integer> intList = Arrays.asList(6, 1, 7, 2, 8, 3, 3, 4, 9, 8, 2, 5, 7, 10);
        Set<Integer> sortedSet = StreamsImpl.of(intList)
                .sorted(Comparator.naturalOrder())
                .toSet();
        Set<Integer> equalSet = new HashSet<>(intList);
        Assertions.assertIterableEquals(sortedSet, equalSet);
    }

    @Test
    @DisplayName("Check that source value stays immutable")
    void stayImmutable() {
        List<Integer> intList = Arrays.asList(6, 1, 7, 2, 8, 3, 3, 4, 9, 8, 2, 5, 7, 10);
        List<Integer> intList2 = Arrays.asList(6, 1, 7, 2, 8, 3, 3, 4, 9, 8, 2, 5, 7, 10);
        List<Integer> intListNew = StreamsImpl.of(intList)
                .filter(integer -> integer % 2 == 0)
                .transform(integer -> integer + 2)
                .sorted(Comparator.naturalOrder())
                .toList();
        Assertions.assertIterableEquals(intList, intList2);
    }

}