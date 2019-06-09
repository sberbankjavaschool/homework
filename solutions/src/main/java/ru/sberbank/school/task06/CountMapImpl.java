package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountMapImpl implements CountMap<String> {

    private Integer count;
    private Map<String,Integer> countHashMap;
    CountMapImpl(){
        countHashMap = new HashMap<>();
    }

    @Override
    public void add(String o) {
        count = countHashMap.get(o);
        countHashMap.put(o, (count == null ? 1 : ++count));
    }

    @Override
    public int getCount(String o) {
        return countHashMap.get(o);
    }

    @Override
    public int remove(String o) {
        return countHashMap.remove(o);
    }

    @Override
    public int size() {
        return countHashMap.size();
    }

    @Override
    public void addAll(CountMap<? extends String> source) {
        Set<String> keySource = source.toMap().keySet();
        for (String key: keySource) {
            add(key);
        }
    }

    @Override
    public Map toMap() {
        return countHashMap;
    }

    @Override
    public void toMap(Map<? super String, Integer> destination) {
        destination = countHashMap;
    }
}
