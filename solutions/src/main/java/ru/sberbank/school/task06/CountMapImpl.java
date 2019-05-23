package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl implements CountMap<Person> {

    private HashMap<Person, Integer> map = new HashMap<>();

    @Override
    public void add(Person k) {
        Integer v = map.getOrDefault(k, 0);
        map.put(k, ++v);
    }

    @Override
    public int getCount(Person o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(Person o) {
        Integer v = map.getOrDefault(o, 0);
        if (!v.equals(0)) {
            map.remove(o);
        }
        return v;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends Person> source) {
        for (Person p : source.toMap().keySet()) {
            add(p);
        }
    }

    @Override
    public Map<Person, Integer> toMap() {
        HashMap<Person, Integer> cloneMap = new HashMap<>();
        toMap(cloneMap);
        return cloneMap;
    }

    @Override
    public void toMap(Map<? super Person, Integer> destination) {
        for (Person key : map.keySet()) {
            destination.put(new Person(key), map.get(key));
        }
    }

    public static void main(String[] args) {
        CountMap<Person> countMap = new CountMapImpl();
        Person vasya = new Person(23, "Vasya", 89032781654L);
        Person gena = new Person(49, "Gena", 89042363854L);
        Person boris = new Person(25, "Boris", 89631234554L);
        countMap.add(vasya);
        countMap.add(gena);
        countMap.add(boris);
        countMap.add(vasya);

        System.out.println("Size: " + countMap.size());
        System.out.println("Count Vasya: " + countMap.getCount(new Person(23, "Vasya", 89032781654L)));
        System.out.println("Count Gena : " + countMap.getCount(gena));
        System.out.println();

        Map<Person, Integer> map = countMap.toMap();
        map.put(vasya, 50);
        System.out.println("Map size: " + map.size());

    }

}


