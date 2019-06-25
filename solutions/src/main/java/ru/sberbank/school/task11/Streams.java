package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}
 */
public class Streams<T> {
    private List<T> list = new ArrayList<>();

    private Streams(@NonNull T... elements) {
        list.addAll(Arrays.asList(elements));
    }

    private Streams(@NonNull Collection<T> elements) {
        list.addAll(elements);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(@NonNull T... elements) {
        return new Streams<>(elements);
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(@NonNull Collection<T> elements) {
        return new Streams<>(elements);
    }

    /**
     * Фильтрация элементов по заданному правилу. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * <p>
     * Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     * <p>
     * Аналог Stream#map(Function).
     * <p>
     * Intermediate операция.
     *
     * @param object - правило фильтрации элементов.
     * @return стрим
     */
    public Streams<T> filter(@NonNull Predicate<T> object) {
        Function<List<T>, List<T>> operation = (toFilter) -> {
            List<T> result = new ArrayList<>();
            for (T it : toFilter) {
                if (object.test(it)) {
                    result.add(it);
                }
            }
            return result;
        };
        return new Streams<>(operation.apply(list));
    }

    /**
     * Преобразование элементов в какие-то другие элементы. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * <p>
     * Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     * <p>
     * Аналог Stream#map.
     * <p>
     * Intermediate операция.
     *
     * @param function - правило траснформации элементов.
     * @return стрим
     */
    public <R> Streams<R> transform(@NonNull Function<T, R> function) {
        Function<List<T>, List<R>> operation = (toTransform) -> {
            List<R> result = new ArrayList<>();
            for (T it : toTransform) {
                result.add(function.apply(it));
            }
            return result;
        };
        return new Streams<>(operation.apply(list));
    }

    /**
     * Сортировка элементов. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * <p>
     * Данный метод должен уметь сортировать элементы по заданному правилу.
     * <p>
     * Аналог Stream#sorted
     * <p>
     * Intermediate операция.
     *
     * @param comparator - правило сортировки элементов.
     * @return стрим
     */
    public Streams<T> sorted(@NonNull Comparator<? super T> comparator) {
        Function<List<T>, List<T>> operation = (toSort) -> {
            toSort.sort(comparator);
            return toSort;
        };
        return new Streams<>(operation.apply(list));
    }

    /**
     * Преобразование стрима в Map. Необходимо подобрать нужные интерфейсы для передачи в этот метод.
     * <p>
     * Данный метод должен уметь преобразовать стрим в Map используя keyMapper и valueMapper.
     * <p>
     * Terminate операция.
     *
     * @param keyMapper   - правило создания ключа.
     * @param valueMapper - правило создания значения.
     * @return Map, собранная по правилам keyMapper и valueMapper
     */
    public <K, V> Map<K, V> toMap(@NonNull Function<T , K> keyMapper,@NonNull  Function<T , V> valueMapper) {
        List<T> resultList = new ArrayList<>(list);
        Map<K, V> map = new HashMap<>();
        for (T it : resultList) {
            map.put(keyMapper.apply(it), valueMapper.apply(it));
        }
        return map;
    }

    /**
     * Преобразование стрима в Set.
     * <p>
     * Данный метод должен уметь преобразовать стрим в Set. Обратите внимание, что метод sorted должен учитываться
     * при вызове этого метода.
     * <p>
     * Terminate операция.
     *
     * @return Set элементов
     */
    public Set toSet() {
        return new LinkedHashSet(list);
    }

    /**
     * Преобразование стрима в List.
     * <p>
     * Данный метод должен уметь преобразовать стрим в List.
     * <p>
     * Terminate операция.
     *
     * @return List элементов
     */
    public List toList() {
        return new ArrayList<>(list);
    }
}