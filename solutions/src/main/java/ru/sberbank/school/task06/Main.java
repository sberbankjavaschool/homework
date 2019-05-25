package ru.sberbank.school.task06;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        CountMapImpl<String> countMap = new CountMapImpl<>();
        String key = "key";
        System.out.println(countMap.remove(key));
        System.out.println(countMap.getCount(key));
        countMap.add(key);
        System.out.println(countMap.getCount(key));
        countMap.add(key);
        System.out.println(countMap.getCount(key));

        System.out.println(CollectionUtils.range(Arrays.asList(8, 1, 3, 5, 6, 4), 3, 6));
    }
}
