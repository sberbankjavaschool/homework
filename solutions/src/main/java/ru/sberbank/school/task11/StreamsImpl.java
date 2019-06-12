package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}
 */
public class StreamsImpl<T> {
    private List<T> myList = new ArrayList<>();


    private StreamsImpl(@NonNull T... elements) {
        myList = Arrays.asList(elements);
    }

    private StreamsImpl(@NonNull Collection<? extends T> elements) {
        myList.addAll(elements);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     * @param elements входные элементы
     * @return стрим элементов из elements
     */

    public static <T> StreamsImpl<T> of(@NonNull T... elements) {
        return new StreamsImpl(elements);
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     * @param elements входные элементы
     * @return стрим элементов из elements
     */

    public static <T> StreamsImpl<T> of(@NonNull Collection<T> elements) {
        return new StreamsImpl(elements);
    }

    /**
     * Фильтрация элементов по заданному правилу. Необходимо подобрать нужный интерфейс для передачи в этот метод.
     * Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     * Аналог Stream#map(Function).
     * Intermediate операция.
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */

    public StreamsImpl<T> filter(@NonNull Predicate<? super T> predicate) {
        List<T> tList = new ArrayList<>();
        for (T elem : myList) {
            if (predicate.test(elem)) {
                tList.add(elem);
            }
        }
        return new StreamsImpl(tList);
    }

    /**
     * Преобразование элементов в какие-то другие элементы.
     * Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     * Аналог Stream#map.
     * Intermediate операция.
     * @param function - правило траснформации элементов.
     * @return стрим
     */

    public <R> StreamsImpl<R> transform(@NonNull Function<? super T, ? extends R> function) {
        List<R> tList = new ArrayList<>();
        for (T elem : myList) {
            tList.add(function.apply(elem));
        }
        return new StreamsImpl(tList);
    }

    /**
     * Сортировка элементов. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Данный метод должен уметь сортировать элементы по заданному правилу.
     * Аналог Stream#sorted
     * Intermediate операция.
     * @param comparator - правило сортировки элементов.
     * @return стрим
     */

    public StreamsImpl<T> sorted(@NonNull Comparator<? super T> comparator) {
        List<T> tList = new ArrayList<>(myList);
        tList.sort(comparator);
        return new StreamsImpl(tList);
    }

    /**
     * Преобразование стрима в Map. Необходимо подобрать нужные интерфейсы для передачи в этот метод.
     * Данный метод должен уметь преобразовать стрим в Map используя keyMapper и valueMapper.
     * Terminate операция.
     * @param keyMapper   - правило создания ключа.
     * @param valueMapper - правило создания значения.
     * @return Map, собранная по правилам keyMapper и valueMapper
     */

    public <K, V> Map<K, V> toMap(@NonNull Function<? super T, ? extends K> keyMapper,
                                  @NonNull Function<? super T, ? extends V> valueMapper) {
        Map<K, V> map = new HashMap<>();
        for (T elem : myList) {
            map.put(keyMapper.apply(elem), valueMapper.apply(elem));
        }
        return map;
    }

    /**
     * Преобразование стрима в Set.
     * Данный метод должен уметь преобразовать стрим в Set. Обратите внимание, что метод sorted должен учитываться
     * при вызове этого метода.
     * Terminate операция.
     * @return Set элементов
     */

    public Set<T> toSet() {
        Set<T> set = new HashSet<>(myList);
        return set;
    }

    /**
     * Преобразование стрима в List.
     * Данный метод должен уметь преобразовать стрим в List.
     * Terminate операция.
     * @return List элементов
     */
    public List<T> toList() {
        return new ArrayList(myList);
    }

}