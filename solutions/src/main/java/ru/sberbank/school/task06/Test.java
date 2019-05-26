package ru.sberbank.school.task06;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        //Part1
        Player p1 = new Player("Ivanov", "AkBars", 18);
        Player p2 = new Player("Belov", "SKA", 27);
        Player p3 = new Player("Kovalchuk", "SKA", 32);
        Player p4 = new Player("Kaprizov", "CSKA", 20);

        CountMap<Player> plMap = new CountMapImpl();
        plMap.add(p1);
        plMap.add(p2);
        plMap.add(p3);
        plMap.add(p4);
        CountMap<Player> plMap2 = new CountMapImpl();
        plMap2.addAll(plMap);
        System.out.println(plMap2.size());
        plMap2.remove(p2);
        System.out.println(plMap2.size());
        Map<Player, Integer> toMap = plMap.toMap();

        // Part2
        CollectionUtils<Integer> collectionUtils = new CollectionUtils<>();
        List<Integer> list3 = collectionUtils.newArrayList();
        List<Integer> list1 = new ArrayList<>(Arrays.asList(23, 45, 2, 7, 6, 5, 8));
        List<Integer> list2 = new ArrayList<>();
        list3 = collectionUtils.limit(list1, 3);
        collectionUtils.addAll(list1, list2);
        int index1 = collectionUtils.indexOf(list2, 7);
        int index2 = collectionUtils.indexOf(list2, 22);
        collectionUtils.add(list2, 11);
        collectionUtils.removeAll(list1, list3);
        boolean containsAll = collectionUtils.containsAll(list1, list3);
        boolean containsAny = collectionUtils.containsAny(list3, list1);

        List<Integer> fromMintoMax = collectionUtils.range(Arrays.asList(8, 1, 3, 5, 6, 9, 4), 3, 9);


    }





}
