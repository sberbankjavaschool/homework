package ru.sberbank.school.task06;

public class Main {
    public static void main(String[] args) {

        CountMapImpl<Integer> countMap = new CountMapImpl();
        countMap.add(1);
        countMap.add(2);
        countMap.add(3);
        countMap.add(2);
        countMap.add(1);
        countMap.add(2);
        System.out.println(countMap.size());
        System.out.println(countMap.getCount(2));
        System.out.println(countMap.remove(2));
        System.out.println(countMap.size());
        String str = null;
    }
}
