package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl implements CountMap<Pet> {
    private Map<Pet, Integer> map = new HashMap<>();

    @Override
    public void add(Pet pet) {
        Integer count = map.putIfAbsent(pet, 1);

        if (count != null) {
            map.put(pet, count + 1);
        }
    }

    @Override
    public int getCount(Pet pet) {
        return map.getOrDefault(pet, 0);
    }

    @Override
    public int remove(Pet pet) {
        if (!map.containsKey(pet)) {
            return 0;
        }
        return map.remove(pet);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends Pet> source) {
        for (Map.Entry<? extends Pet, Integer> entry : source.toMap().entrySet()) {
            Integer count = map.putIfAbsent(entry.getKey(), entry.getValue());

            if (count != null) {
                map.put(entry.getKey(), count + entry.getValue());
            }
        }
    }

    @Override
    public Map<Pet, Integer> toMap() {
        return new HashMap<>(map);
    }

    @Override
    public void toMap(Map<? super Pet, Integer> destination) {
        destination.putAll(map);
    }
}
