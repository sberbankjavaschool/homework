package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class ICountMapImpl<T> implements ICountMap<T> {
    private Map<? super T,Integer> map = new HashMap<>();

    @Override
    public void add(T o) {
        if (map.containsKey(o)) {
            map.put(o,map.get(o) + 1);
        } else {
            map.put(o, 1);
        }
    }

    @Override
    public int getCount(T o) {
        if (map.containsKey(o)) {
            return map.get(o);
        }
        return 0;
    }

    @Override
    public int remove(T o) {
        int count = 0;
        if (map.containsKey(o)) {
            count = map.get(o);
            map.remove(o);
        }
        return count;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(ICountMap<T> source) {
        Map src = source.toMap();
        for (Object key : src.keySet()) {
            if (map.containsKey(key)) {
                map.put((T) key, this.getCount((T) key) + (int) src.get(key));
            } else {
                map.put((T) key, (int) src.get(key));
            }
        }
    }

    @Override
    public Map<? super T, Integer> toMap() {
        return map;
    }

    @Override
    public void toMap(Map destination) {
        destination.clear();
        destination.putAll(map);
    }
}
