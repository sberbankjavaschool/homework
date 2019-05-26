package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

public class CountMapImpl<K> implements CountMap<K> {


    private Map<K, Integer> innerMap = new HashMap<>();

    /**
     * Добавление элепмента в контейнер.
     *
     * @param o элемент для добавления
     */
    @Override
    public void add(K o) {
        if (innerMap.containsKey(o)) {
            innerMap.put(o, innerMap.get(o) + 1);
        } else {
            innerMap.put(o, 1);
        }
    }

    /**
     * Получение количества вхождений данного элемента.
     *
     * @param o элемент
     * @return количество вхождений эелемента
     */
    @Override
    public int getCount(K o) {
        if (innerMap.containsKey(o)) {
            return innerMap.get(o);
        }
        return 0;
    }

    /**
     * Удаление элемента из контейнера (полностью).
     *
     * @param o элемент
     * @return количество добавлений элемента(до удаления)
     */
    @Override
    public int remove(K o) {
        int count = getCount(o);
        innerMap.remove(o);
        return count;
    }

    /**
     * Получение количества разных элементов.
     *
     * @return количество разных элементов.
     */
    @Override
    public int size() {
        return innerMap.size();
    }

    /**
     * Добавление всех элементов из коллекции source в текущий контейнер. При совпадениии ключей,
     * счётчики увеличивается.
     *
     * @param source коллекция - источник
     */
    @Override
    public void addAll(CountMap<? extends K> source) {
        for (Map.Entry<? extends K, Integer> entry : source.toMap().entrySet()) {
            if (innerMap.containsKey(entry.getKey())) {
                innerMap.put(entry.getKey(), getCount(entry.getKey()) + entry.getValue());
            } else {
                innerMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Трансформация текущего контейнеру к контейнеру java.util.Map. Ключ - добавленный элемент,
     * значение - количество его добавлений.
     *
     * @return контейнер java.util.Map
     */
    @Override
    public Map<K, Integer> toMap() {
        return new HashMap<>(innerMap);
    }

    /**
     * Тот же контракт, что и у toMap(), но результат записать в destination.
     *
     * @param destination мапа, в которую пишем значения
     */
    @Override
    public void toMap(Map<? super K, Integer> destination) {
        destination.clear();
        destination.putAll(innerMap);
    }
}
