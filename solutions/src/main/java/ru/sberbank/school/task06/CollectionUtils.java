package ru.sberbank.school.task06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionUtils<E> {

    /**
     * Добавление всех элементов одной коллекции в другую.
     *
     * @param source      источник
     * @param destination потребитель
     */
    public static <E> void addAll(List<? extends E> source, List<? super E> destination) {
        destination.addAll(source);
    }

    /**
     * Инициализация листа с реализаций java.util.ArrayList.
     *
     * @return пустой ArrayList
     */
    public static <E> List<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Поиск индекса элемента в списке.
     *
     * @param source список, в котором искать элемент
     * @param o      элемент
     * @return индекс элемента
     */
    public static <E> int indexOf(List<? super E> source, E o) {
        return source.indexOf(o);
    }

    /**
     * Обрезание списка заданным числом элементов. limit({1, 2 ,3}, 2) => {1, 2}
     *
     * @param source список
     * @param size   необходимое число элементов
     * @return сокращенный список
     */
    public static <E> List<E> limit(List<E> source, int size) {
        return source.size() > size ? source.subList(0, size) : source;
    }

    /**
     * Добавление элемента в коллекцию.
     *
     * @param dest коллекция, в которую необходимо вставить элемент
     * @param o    элемент
     */
    public static <E> void add(List<? super E> dest, E o) {
        dest.add(o);
    }


    /**
     * Удаление всех элементов одной коллекии из другой.
     *
     * @param removeFrom коллекция, из которой удалять элементы
     * @param toRemove   коллекция, в которой лежат элементы для удаления из коллекции removeFrom
     */
    public static <E> void removeAll(List<? super E> removeFrom, List<? extends E> toRemove) {
        removeFrom.removeAll(toRemove);
    }

    /**
     * Проверка на содержение одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если все элементы коллекции c2 содержатся в c1
     */
    public static <E> boolean containsAll(List<? super E> c1, List<? extends E> c2) {
        return c1.containsAll(c2);
    }

    /**
     * Проверка на содержение хотя бы одного элемента одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если хотя бы 1 элемент коллекции c2 содержатся в c1
     */
    public static <E> boolean containsAny(List<? super E> c1, List<? extends E> c2) {
        for (E e : c2) {
            if (c1.contains(e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Создание коллекции, содержащей элементы из входной коллекции в диапазоне от min до max (не
     * индексы, а значения). Элементы необходимо сравнивать, через интерфейс Comparable Пример:
     * range(Arrays.asList(8, 1, 3, 5, 6, 4), 3, 6) вернет {3,4,5,6}
     *
     * @param list исходная коллекция
     * @param min  минимальное значение
     * @param max  максимальнео значение
     * @return отфильрованный по минимальному и максимальному значению список
     */
    public static <E extends Comparable<? super E>> List<E> range(List<? extends E> list, E min, E max) {
        return list.stream()
                .filter(e -> e.compareTo(min) >= 0 && e.compareTo(max) <= 0)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Создание коллекции, содержащей элементы из входной коллекции в диапазоне от min до max (не
     * индексы, а значения). Элементы необходимо сравнивать, через интерфейс Comparator Пример:
     * range(Arrays.asList(8, 1, 3, 5, 6, 4), 3, 6) вернет {3,4,5,6}
     *
     * @param list исходная коллекция
     * @param min  минимальное значение
     * @param max  максимальнео значение
     * @return отфильрованный по минимальному и максимальному значению список
     */
    public static <E> List<E> range(List<? extends E> list, E min, E max, Comparator<? super E> comparator) {
        return list.stream()
                .filter(e -> comparator.compare(e, min) >= 0 && comparator.compare(e, max) <= 0)
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}