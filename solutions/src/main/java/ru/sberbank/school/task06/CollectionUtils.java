package ru.sberbank.school.task06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
     * Инициализация листа с реализаций java.uXtil.ArrayList.
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
    public static <T> int indexOf(List<? extends T> source, T o) {
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
        return (source.size() > size) ? (source.subList(0, size)) : newArrayList();
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
    public static boolean containsAll(List<?> c1, List<?> c2) {
        return c1.containsAll(c2);
    }

    /**
     * Проверка на содержение хотя бы одного элемента одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если хотя бы 1 элемент коллекции c2 содержатся в c1
     */
    public static boolean containsAny(List<?> c1, List<?> c2) {
        for (Object o2 : c2) {
            if (c1.contains(o2)) {
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
    public static <T extends Comparable<T>> List range(List<? extends T> list, Object min, Object max) {
        ArrayList<T> result = new ArrayList<>();
        for (T o : list) {
            if (o.compareTo((T) min) >= 0 && o.compareTo((T)max) <= 0) {
                result.add(o);
            }
        }
        return result;
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
    public static <T> List range(List<? extends T> list, Object min, Object max, Comparator comparator) {
        ArrayList<T> result = new ArrayList<>();
        for (T o : list) {
            if (comparator.compare(o, min) >= 0 && comparator.compare(o, max) <= 0) {
                result.add(o);
            }
        }
        return result;
    }
}
