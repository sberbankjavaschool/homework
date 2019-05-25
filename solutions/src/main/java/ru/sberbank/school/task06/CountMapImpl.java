package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl implements CountMap<String> {

    private Map<String, Integer> map = new HashMap<>();

    @Override
    public void add(String key) {
        map.merge(key, 1, Integer::sum);
    }

    @Override
    public int getCount(String key) {
        return map.getOrDefault(key, 0);
    }

    @Override
    public int remove(String key) {
        Integer value = map.remove(key);
        return value == null ? 0 : value;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends String> source) {
        for (String key : source.toMap().keySet()) {
            add(key);
        }
    }

    @Override
    public Map<String, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super String, Integer> destination) {
        destination.putAll(map);
    }
}
