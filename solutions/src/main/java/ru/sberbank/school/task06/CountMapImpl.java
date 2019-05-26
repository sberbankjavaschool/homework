package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {

    private Map<T, Integer> innerMap = new HashMap<>();

    public void add(T element, Integer count) {
        Integer oldCounter = innerMap.getOrDefault(element, 0);
        innerMap.put(element, oldCounter + count);
    }

    @Override
    public void add(T element) {
        Integer counter = innerMap.getOrDefault(element, 0);
        innerMap.put(element, ++counter);
    }

    @Override
    public int getCount(T element) {
        return innerMap.getOrDefault(element, 0);
    }

    @Override
    public int remove(T element) {
        if (innerMap.containsKey(element)) {
            return innerMap.remove(element);
        } else {
            return 0;
        }
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public void addAll(CountMap<? extends T> source) {
        for (Map.Entry<? extends T, Integer> element : source.toMap().entrySet()) {
            this.add(element.getKey(), element.getValue());
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(innerMap);
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
        destination.putAll(innerMap);
    }

    @Override
    public String toString() {
        return innerMap.toString();
    }
}
