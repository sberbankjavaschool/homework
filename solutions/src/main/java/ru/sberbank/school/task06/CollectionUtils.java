package ru.sberbank.school.task06;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;

public class CollectionUtils<T> {

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
        if(source.size() == 0 || source.size() < size) {
            return source;
        }
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
    public static <T> void removeAll(List<? super T> removeFrom, List<? extends T> toRemove) {                               //???
        Iterator iterator = toRemove.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (removeFrom.contains(element)) {
                iterator.remove();
            }
        }
    }

    /**
     * Проверка на содержение одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если все элементы коллекции c2 содержатся в c1
     */
    public static <T> boolean containsAll(List<?> c1, List<?> c2) {
        return c1.containsAll(c2);
    }

    /**
     * Проверка на содержение хотя бы одного элемента одной коллекции в другой.
     *
     * @param c1 первая коллекция
     * @param c2 вторая коллекция
     * @return true, если хотя бы 1 элемент коллекции c2 содержатся в c1
     */
    public static <T> boolean containsAny(List<?> c1, List<?> c2) {
        for (Object element : c2) {
            if (c1.contains(element)) {
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
    public static <T extends Comparable<? super T>> List<T> range(List<? extends T> list, T min, T max) {
         List<T> newlist = new ArrayList<>();
         for (T element : list) {
             if (element.compareTo(min) >= 0 && element.compareTo(max) <= 0) {
                 if (!newlist.isEmpty()) {
                     int index = newlist.size();
                     if (element.compareTo(newlist.get(index-1))<0) {
                         index = findIndex(newlist, element);
                     }
                     newlist.add(index, element);
                 }  else {
                     newlist.add(element);
                 }
             }
         }
         return newlist;
    }

    private static <T extends Comparable<? super T>> int findIndex(List<T> newlist, T element) {
        int index = 0;
        for (T element1 : newlist) {
            if (element.compareTo(element1) <= 0) {
                index = newlist.indexOf(element1);
                break;
            }
        }
        return index;
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
    public static <T>List<T> range(List<? extends T> list, T min, T max, Comparator<? super T> comparator) {
        List<T> newlist = new ArrayList<>();
        for (T element : list) {
            if (comparator.compare(element, min) >= 0 && comparator.compare(element, max) <= 0) {
                if (!newlist.isEmpty()) {
                    int index = 0;
                    if (comparator.compare(element, newlist.get(newlist.size()-1))<0) {
                        for (T element1 : newlist) {
                            if (comparator.compare(element, element1) <= 0) {
                                index = newlist.indexOf(element1);
                                break;
                            }
                        }
                        newlist.add(index, element);
                    } else {
                        newlist.add(element);
                    }
                }  else {
                    newlist.add(element);
                }
            }
        }

        return newlist;
    }

}
