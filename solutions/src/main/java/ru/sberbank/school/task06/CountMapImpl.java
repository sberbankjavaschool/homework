package ru.sberbank.school.task06;

import java.util.HashMap;
import java.util.Map;

/**25.05.2019
 * Реализация CountMap с подсчетом количества вхождений одинаковых элементов на базе HashMap
 *
 * @author Gregory Melnikov, created on 24.05.19
 */
public class CountMapImpl<O> implements CountMap<O> {

    private transient HashMap<O, Integer> map;

    public CountMapImpl() {
        map = new HashMap<>();
    }

    /**
     * Добавление элемента в контейнер.
     *
     * @param o элемент для добавления
     */
    @Override
    public void add(O o) {
        int counter = 0;
        if (map.containsKey(o)) {
            counter = map.get(o);
        }
        map.put(o, ++counter);
    }

    /**
     * Получение количества вхождений данного элемента.
     *
     * @param o элемент
     * @return количество вхождений эелемента
     */
    @Override
    public int getCount(O o) {
        return map.get(o);
    }

    /**
     * Удаление элемента из контейнера (полностью).
     *
     * @param o элемент
     * @return количество добавлений элемента(до удаления)
     */
    @Override
    public int remove(O o) {
        int counter = map.get(o);
        map.remove(o, counter);
        return counter;
    }

    /**
     * Получение количества разных элементов.
     *
     * @return количество разных элементов.
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Добавление всех элементов из коллекции source в текущий контейнер. При совпадениии ключей,
     * счётчики увеличивается.
     *
     * @param source коллекция - источник
     */
    @Override
    public void addAll(CountMap<O> source) {

        Map<O, Integer> entryMap = source.toMap();

        for (Map.Entry<O, Integer> entry : entryMap.entrySet()) {
            O o = entry.getKey();
            int counter = 0;
            if (map.containsKey(o)) {
                counter = map.get(o);
            }
            counter += entry.getValue();
            map.put(o, counter);
        }
    }

    /**
     * Трансформация текущего контейнера к контейнеру java.util.Map. Ключ - добавленный элемент,
     * значение - количество его добавлений.
     *
     * @return контейнер java.util.Map
     */
    @Override
    public Map<O, Integer> toMap() {
        return new HashMap<>(map);
    }

    /**
     * Тот же контракт, что и у toMap(), но результат записать в destination.
     *
     * @param destination контейнер, принимающий результат
     */
    @Override
    public void toMap(Map<? super O, Integer> destination) {
        destination.putAll(map);
    }
}