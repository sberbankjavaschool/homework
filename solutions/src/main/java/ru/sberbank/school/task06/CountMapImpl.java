package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {

    private final Map<T, Integer> map;

    public CountMapImpl() {
        this.map = new HashMap<>();
    }

    @Override
    public void add(T o) {
        Integer count = map.get(o);
        map.put(o, count == null ? 1 : count + 1);
    }

    public int getCount(T o) {
        return map.getOrDefault(o, 0);

    }

    @Override
    public int remove(T o) {
//        if (map.containsKey(o)){
//            return map.remove(o);
//        } else {
//            return 0;
//        }
        return (map.getOrDefault(o, 0).equals(0)) ? 0 : map.remove(o);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends T> source) {

        for (Map.Entry<? extends T, Integer> e : source.toMap().entrySet()) {
            if (map.containsKey(e.getKey())) {
                map.put(e.getKey(), map.get(e.getKey()) + e.getValue());
            } else {
                map.put(e.getKey(), e.getValue());
            }

        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
 //       if (!(destination == null)) {
            destination.putAll(map);
 //       }
    }

    @Override
    public String toString() {
        return "CountMapImpl{" +
                "map=" + map +
                '}';
    }
}
