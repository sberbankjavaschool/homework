package ru.sberbank.school.task06;

import java.util.Comparator;
import java.util.List;

public class CollectionUtils {

    /**
     * Добавление всех элементов одной коллекции в другую.
     *
     * @param source      источник
     * @param destination потребитель
     */
    public static void addAll(List source, List destination) {
        throw new UnsupportedOperationException();
    }

    /**
     * Инициализация листа с реализаций java.uXtil.ArrayList.
     *
     * @return пустой ArrayList
     */
    public static List newArrayList() {
        throw new UnsupportedOperationException();
    }

    /**
     * Поиск индекса элемента в списке.
     *
     * @param source список, в котором искать элемент
     * @param o      элемент
     * @return индекс элемента
     */
    public static int indexOf(List source, Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * Обрезание списка заданным числом элементов. limit({1, 2 ,3}, 2) => {1, 2}
     *
     * @param source список
     * @param size   необходимое число элементов
     * @return сокращенный список
     */
    public static List limit(List source, int size) {
        throw new UnsupportedOperationException();
    }

    /**
     * Добавление элемента в коллекцию.
     *
     * @param dest коллекция, в которую необходимо вставить элемент
     * @param o    элемент
     */
    public static void add(List dest, Object o) {
        throw new UnsupportedOperationException();
    }


    /**
     * Удаление всеъх элементов одной коллекии из другой.
     *
     * @param removeFrom коллекция, из которой удалять элементы
     * @param toRemove   коллекция, в которой лежат элементы для удаления из коллекции removeFrom
     */
    public static void removeAll(List removeFrom, List toRemove) {
        throw new UnsupportedOperationException();
    }

    /**
     * Проверка на содержение одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если все элементы коллекции c2 содержатся в c1
     */
    public static boolean containsAll(List c1, List c2) {
        throw new UnsupportedOperationException();
    }

    /**
     * Проверка на содержение хотя бы одного элемента одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если хотя бы 1 элемент коллекции c2 содержатся в c1
     */
    public static boolean containsAny(List c1, List c2) {
        throw new UnsupportedOperationException();
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
    public static List range(List list, Object min, Object max) {
        throw new UnsupportedOperationException();
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
    public static List range(List list, Object min, Object max, Comparator comparator) {
        throw new UnsupportedOperationException();
    }

}
