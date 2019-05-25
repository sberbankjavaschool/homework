package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {

    private Map<T, Integer> map = new HashMap<>();


    @Override
    public void add(T o) {

        Integer count = map.getOrDefault(o, 0);
        map.put(o, ++ count);

    }

    @Override
    public int getCount(T o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(T o) {
        if (!map.containsKey(o)) {
            return 0;
        }
        return map.remove(o);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends T> source) {
        for (T s : source.toMap().keySet()) {
            add(s);
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
        destination.putAll(toMap());
    }
}
