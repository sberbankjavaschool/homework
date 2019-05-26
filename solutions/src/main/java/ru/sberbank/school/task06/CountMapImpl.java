package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<T> implements CountMap<T> {
    private Map<T, Integer> countMap = new HashMap<>();

    @Override
    public void add(T o) {
        countMap.merge(o, 1, (oldVal, newVal) -> countMap.get(o) + 1);
    }

    @Override
    public int getCount(T o) {
        return countMap.getOrDefault(o, 0);
    }

    @Override
    public int remove(T o) {
        int result = getCount(o);
        countMap.remove(o);

        return result;
    }

    @Override
    public int size() {
        return countMap.size();
    }

    @Override
    public void addAll(CountMap<? extends T> source) {
        for (Map.Entry<? extends T, Integer> entry : source.toMap().entrySet()) {
            countMap.merge(entry.getKey(), entry.getValue(),
                    (oldVal, newVal) -> countMap.get(entry.getKey()) + entry.getValue());
        }
    }

    @Override
    public Map<T, Integer> toMap() {
        return new HashMap<>(countMap);
    }

    @Override
    public void toMap(Map<? super T, Integer> destination) {
        destination.putAll(toMap());
    }
}
