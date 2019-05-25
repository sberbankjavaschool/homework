package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<K> implements CountMap<K> {

    private Map<K,Integer> map = new HashMap<>();

    @Override
    public void add(K o) {
        map.merge(o, 1, Integer::sum);
    }

    @Override
    public int getCount(K o) {
        return map.getOrDefault(o, 0);
    }

    @Override
    public int remove(K o) {
        int count = getCount(o);
        map.remove(o);
        return count;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends K> source) {
        map.putAll(source.toMap());
    }

    @Override
    public Map<K, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super K, Integer> destination) {
        destination.putAll(toMap());
    }
}
