package ru.sberbank.school.task06;

import ru.sberbank.school.task06.Player;

import java.time.Period;
import java.util.HashMap;
import java.util.Map;

/**
 * Интерфейс мапы, которая ведет подсчёт вхождений элемента в контейнер.
 *
 *
 * Пример(без дженериков):
 *
 * CountMap map = new ... ();
 *
 * map.add("Some word");
 *
 * map.add("Some word");
 *
 *
 *
 * ==> map{"Some word" : 2}
 */
public class CountMapImpl implements CountMap<Player> {

    private HashMap<Player, Integer> playersMap = new HashMap<>();
    /**
     * Добавление элепмента в контейнер.
     *
     * @param p элемент для добавления
     */
    public void add(Player p) {
        playersMap.put(p, playersMap.getOrDefault(p, 0)+1);
    }

    /**
     * Получение количества вхождений данного элемента.
     *
     * @param p элемент
     * @return количество вхождений эелемента
     */
    public int getCount(Player p) {
        return playersMap.getOrDefault(p, 0);
    }

    //Удаляет элемент и контейнера и возвращает количество его добавлений(до удаления)

    /**
     * Удаление элемента из контейнера (полностью).
     *
     * @param p элемент
     * @return количество добавлений элемента(до удаления)
     */
    public int remove(Player p) {
        int count = playersMap.getOrDefault(p, 0);
        if (count == 0) {
            return 0;
        } else {
            playersMap.remove(p);
            return count;
        }
    }

    /**
     * Получение количества разных элементов.
     *
     * @return количество разных элементов.
     */
    public int size() {
        return playersMap.size();
    }

    /**
     * Добавление всех элементов из коллекции source в текущий контейнер. При совпадениии ключей,
     * счётчики увеличивается.
     *
     * @param source коллекция - источник
     */
    public void addAll(CountMap<? extends Player> source) {
        for (Player p : source.toMap().keySet()) {
            this.add(p);
        }

    }

    //Вернуть java.util.Map. ключ - добавленный элемент, значение - количество его добавлений

    /**
     * Трансформация текущего контейнеру к контейнеру java.util.Map. Ключ - добавленный элемент,
     * значение - количество его добавлений.
     *
     * @return контейнер java.util.Map
     */
    public Map<Player, Integer> toMap() {
        HashMap<Player, Integer> pMap = new HashMap<>(this.size());
        for (Player p : playersMap.keySet()) {
            pMap.put(p, playersMap.get(p));
        }
//        for (Map.Entry<Player, Integer> entry : pMap.entrySet()) {
//            pMap.put(entry.getKey(), playersMap.get(entry.getKey()));
//        }
        return pMap;
    }


    /**
     * Тот же контракт, что и у toMap(), но результат записать в destination.
     */

    public void toMap(Map<? super Player, Integer> destination) {
        for (Player p : playersMap.keySet()) {
            destination.put(p, playersMap.get(p));
        }
    }
}

