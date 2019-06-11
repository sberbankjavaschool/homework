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
        return map.get(o);
    }

    @Override
    public int remove(T o) {
        return map.remove(o);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends T> source) {
        for (T t : source.toMap().keySet()) {
            add(t);
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
        destination = new HashMap<>(map);
    }

    @Override
    public String toString() {
        return "CountMapImpl{" +
                "map=" +
                map.toString() +
                '}';
    }
}
