package ru.sberbank.school.task06;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        //Part1
//        Player p1 = new Player("Ivanov", "AkBars", 18);
//        Player p2 = new Player("Belov", "SKA", 27);
//        Player p3 = new Player("Kovalchuk", "SKA", 32);
//        Player p4 = new Player("Kaprizov", "CSKA", 20);
//
//        CountMap<Player> plMap = new CountMapImpl();
//        plMap.add(p1);
//        plMap.add(p2);
//        plMap.add(p3);
//        plMap.add(p4);
//        CountMap<Player> plMap2 = new CountMapImpl();
//        plMap2.addAll(plMap);
//        System.out.println(plMap2.size());
//        plMap2.remove(p2);
//        System.out.println(plMap2.size());

        //Part2

        CollectionUtils<Integer> collectionUtils = new CollectionUtils<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(23);
        list1.add(2);
        list1.add(7);
        list1.add(6);
        list1.add(5);
        list1.add(8);
        collectionUtils.addAll(list1, list2);
        List<Integer> list3 = collectionUtils.newArrayList();
        int index1 = collectionUtils.indexOf(list2, 7);
        int index2 = collectionUtils.indexOf(list2, 22);
        list3 = collectionUtils.limit(list1, 3);
        collectionUtils.add(list2, 11);
        //collectionUtils.removeAll(list1, list3);
        boolean containsAll = collectionUtils.containsAll(list1, list3);
        boolean containsAny = collectionUtils.containsAny(list3, list1);

        List<Integer> fromMintoMax = collectionUtils.range(Arrays.asList(8, 1, 3, 5, 6, 9, 4), 3, 9);
        List<Integer> fromMintoMax2 = collectionUtils.range(Arrays.asList(8, 1, 3, 5, 6, 9, 4), 3, 9);

    }

}
