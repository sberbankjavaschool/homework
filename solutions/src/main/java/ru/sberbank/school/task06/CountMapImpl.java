package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountMapImpl<V> implements CountMap<V> {

    private Integer count;
    private Map<V,Integer> countHashMap;
    CountMapImpl(){
        countHashMap = new HashMap<>();
    }

    @Override
    public void add(V o) {
        count = countHashMap.get(o);
        countHashMap.put(o, (count == null ? 1 : ++count));
    }

    @Override
    public int getCount(V o) {
        return countHashMap.get(o);
    }

    @Override
    public int remove(V o) {
        return countHashMap.remove(o);
    }

    @Override
    public int size() {
        return countHashMap.size();
    }

    @Override
    public void addAll(CountMap source) {
        Set<V> keySource = source.toMap().keySet();
    }

    @Override
    public Map toMap() {
        return countHashMap;
    }

    @Override
    public void toMap(Map destination) {

    }
}
