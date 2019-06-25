package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mart
 * 25.05.2019
 **/
public class MapCounter implements CountMap<Employee> {
    private Map<Employee, Integer> map = new HashMap<>();

    @Override
    public void add(Employee emp) {
        Integer value = map.getOrDefault(emp, 0);
        map.put(emp, ++value);
    }

    private void add(Employee emp, int counter) {
        Integer value = map.getOrDefault(emp, 0);
        map.put(emp, value + counter);
    }

    @Override
    public int getCount(Employee emp) {
        return map.get(emp);
    }

    @Override
    public int remove(Employee emp) {
        int count = map.get(emp);
        map.remove(emp);
        return count;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void addAll(CountMap<? extends Employee> source) {
        for (Employee e : source.toMap().keySet()) {
            add(e);
        }
    }

    @Override
    public Map<Employee, Integer> toMap() {
        Map<Employee, Integer> clone = new HashMap<>();
        toMap(clone);
        return clone;
    }

    @Override
    public void toMap(Map<? super Employee, Integer> destination) {
        for (Employee emp : map.keySet() ) {
            destination.put(new Employee(emp), map.get(emp));
        }
    }
}
