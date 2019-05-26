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
    public static <T> List newArrayList() {
        return new ArrayList<T>();
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
    public static <T> List limit(List<? extends T> source, int size) {
        return source.subList(0, size);
    }

    /**
     * Добавление элемента в коллекцию.
     *
     * @param dest коллекция, в которую необходимо вставить элемент
     * @param o    элемент
     */
    public static <T> void add(List< ? super T > dest, T o) {
        dest.add(o);
    }


    /**
     * Удаление всеъх элементов одной коллекии из другой.
     *
     * @param removeFrom коллекция, из которой удалять элементы
     * @param toRemove   коллекция, в которой лежат элементы для удаления из коллекции removeFrom
     */
    public static <T> void removeAll(List<? super T> removeFrom, List<? super T> toRemove) {
            removeFrom.removeAll(toRemove);
    }

    /**
     * Проверка на содержение одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если все элементы коллекции c2 содержатся в c1
     */
    public static <T> boolean containsAll(List<? extends T> c1, List<? super T> c2) {
        return c1.containsAll(c2);
    }

    /**
     * Проверка на содержение хотя бы одного элемента одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если хотя бы 1 элемент коллекции c2 содержатся в c1
     */
    public static <T> boolean containsAny(List<T> c1, List<T> c2) {
        for (T o : c2) {
            if (c1.contains(o)) {
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
    public static <T extends Object & Comparable<? super T>> List<T> range(List<? extends T> list, T min, T max) {
        List<T> resultList = new ArrayList<>();

        for (T elem : list) {
            if (elem.compareTo(min) >= 0 && elem.compareTo(max) <= 0) {
                if (!resultList.isEmpty()) {
                    int index = resultList.size();
                    if (elem.compareTo(resultList.get(index - 1)) <= 0) {
                        for (T current : resultList) {
                            if (elem.compareTo(current) <= 0) {
                                index = resultList.indexOf(current);
                                break;
                            }
                        }
                        resultList.add(index, elem);
                    } else {
                        resultList.add(elem);
                    }
                } else {
                    resultList.add(elem);
                }
            }
        }
        return resultList;
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
    public static<T> List<T> range(List<? extends T> list, T min, T max, Comparator<? super T> comparator) {
        List<T> resultList = new ArrayList<>();

        for (T elem : list) {
            if (comparator.compare(elem, min) >= 0 && comparator.compare(elem, max) <= 0) {
                if (!resultList.isEmpty()) {
                    int index = resultList.size();
                    if (comparator.compare(elem, resultList.get(index - 1)) <= 0) {
                        for (T current : resultList) {
                            if (comparator.compare(elem, current) <= 0) {
                                index = resultList.indexOf(current);
                                break;
                            }
                        }
                        resultList.add(index,elem);
                    } else {
                        resultList.add(elem);
                    }
                } else {
                    resultList.add(elem);
                }
            }
        }
        return resultList;
    }
}
