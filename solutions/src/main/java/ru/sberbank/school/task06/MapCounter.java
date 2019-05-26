package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mart
 * 25.05.2019
 **/
public class MapCounter <K> implements CountMap {
    private Map <K, Integer> map = new HashMap<>();

    @Override
    public void add(Object o) {
        if (map.containsKey(o)) {
            map.put((K) o, map.get(o) + 1);
        } else {
            map.put((K) o, 1);
        }
    }

    private void add(Object o, int counter) {
        if (map.containsKey(o)) {
            map.put((K) o, map.get(o) + counter);
        } else {
            map.put((K) o, counter);
        }
    }

    @Override
    public int getCount(Object o) {
        return map.get(o);
    }

    @Override
    public int remove(Object o) {
        int count = map.get(o);
        map.remove(o);
        return count;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap source) {
        Map sourceMap = source.toMap();
        for (Object o : sourceMap.keySet()) {
            this.add(o, (Integer) sourceMap.get(o));
        }
    }

    @Override
    public Map<? extends K, Integer> toMap() {
        return map;
    }

    @Override
    public void toMap(Map destination) {
        MapCounter destMap = (MapCounter) destination;
        for (Object o : map.keySet()) {
            destMap.add(o, map.get(o));
        }
    }

}
