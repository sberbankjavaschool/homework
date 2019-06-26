package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountMapImpl implements CountMap<String> {

    private final Map<String,Integer> countHashMap;

    public CountMapImpl() {
        countHashMap = new HashMap<>();
    }

    @Override
    public void add(String o) {
        Integer count = countHashMap.get(o);
        countHashMap.put(o, (count == null ? 1 : ++count));
    }

    @Override
    public int getCount(String o) {
        return countHashMap.get(o);
    }

    @Override
    public int remove(String o) {
        if (countHashMap.containsKey(o)) {
            return countHashMap.remove(o);
        }
        return 0;
    }

    @Override
    public int size() {
        return countHashMap.size();
    }

    @Override
    public void addAll(CountMap<? extends String> source) {
        Map<String,Integer> toMapSource = source.toMap();
        Set<String> keySource = toMapSource.keySet();
        for (String key: keySource) {
            Integer volSource = toMapSource.get(key);
            Integer volChm = countHashMap.getOrDefault(key, 0);
            countHashMap.put(key, volSource + volChm);
        }
    }

    @Override
    public Map<String, Integer> toMap() {
        Map<String, Integer> map = new HashMap<>(countHashMap);
        return map;
    }

    @Override
    public void toMap(Map<? super String, Integer> destination) {
        destination.putAll(countHashMap);
    }
}
