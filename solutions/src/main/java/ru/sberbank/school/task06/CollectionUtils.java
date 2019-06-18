package ru.sberbank.school.task06;

import java.util.*;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

public class CollectionUtils {

    /**
     * Добавление всех элементов одной коллекции в другую.
     *
     * @param source      источник
     * @param destination потребитель
     */
    public static <T> void addAll(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    /**
     * Инициализация листа с реализаций java.util.ArrayList.
     *
     * @return пустой ArrayList
     */
    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    /**
     * Поиск индекса элемента в списке.
     *
     * @param source список, в котором искать элемент
     * @param o      элемент
     * @return индекс элемента
     */
    public static <T> int indexOf(List<? extends T> source, Object o) {
        return source.indexOf(o);
    }

    /**
     * Обрезание списка заданным числом элементов. limit({1, 2 ,3}, 2) => {1, 2}
     *
     * @param source список
     * @param size   необходимое число элементов
     * @return сокращенный список
     */
    public static <T> List<T> limit(List<T> source, int size) {
        return source.subList(0, size);
    }

    /**
     * Добавление элемента в коллекцию.
     *
     * @param dest коллекция, в которую необходимо вставить элемент
     * @param o    элемент
     */
    public static <T> void add(List<? super T> dest, T o) {
        dest.add(o);
    }


    /**
     * Удаление всеъх элементов одной коллекии из другой.
     *
     * @param removeFrom коллекция, из которой удалять элементы
     * @param toRemove   коллекция, в которой лежат элементы для удаления из коллекции removeFrom
     */
    public static <T> void removeAll(List<? super T> removeFrom, List<? extends T> toRemove) {
        removeFrom.removeAll(toRemove);
    }

    /**
     * Проверка на содержение одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если все элементы коллекции c2 содержатся в c1
     */
    public static <T> boolean containsAll(List<? super T> c1, List<? extends T> c2) {
        return c1.containsAll(c2);
    }

    /**
     * Проверка на содержение хотя бы одного элемента одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если хотя бы 1 элемент коллекции c2 содержатся в c1
     */
    public static <T> boolean containsAny(List<? super T> c1, List<? extends T> c2) {
        for (Object o : c1) {
            for (T t : c2) {
                if (o.equals(t)) {
                    return true;
                }

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
    public static <T extends Comparable<? super T>> List<T> range(List<? extends T> list, T min, T max) {
//        TreeSet<T> set = new TreeSet<T>() {{
//                for (T t : list) {
//                    if (t.compareTo(min) >= 0 && t.compareTo(max) <= 0) {
//                        add(t);
//                    }
//                }
//            }};

        return list.stream()
                .filter((T t) -> t.compareTo(min) >= 0 && t.compareTo(max) <= 0)
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
    public static <T> List<T> range(List<? extends T> list, T min, T max, Comparator<? super T> comparator) {
//        TreeSet<T> set = new TreeSet<T>() {{
//                for (T t : list) {
//                    if (comparator.compare(t, min) >= 0 && comparator.compare(t, max) <= 0) {
//                        add(t);
//                    }
//                }
//
//            }};

        return list.stream()
                .filter((T t) -> comparator.compare(t, min) >= 0 && comparator.compare(t, max) <= 0)
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
