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
    private final List<T> list = new ArrayList<>();

    private final List<Function <List<T>, List<?>>> functions = new ArrayList<>();

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
        functions.add((toFilter) -> {
            List<T> result = new ArrayList<>();
            for (T it : toFilter) {
                if (object.test(it)) {
                    result.add(it);
                }
            }
            return result;
        });
        return this;
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
    @SuppressWarnings("unchecked")
    public <R> Streams<R> transform(@NonNull Function<T, R> function) {
        functions.add((toTransform) -> {
            List<R> result = new ArrayList<>();
            for (T it : toTransform) {
                result.add(function.apply(it));
            }
            return result;
        });
        return (Streams<R>) this;
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
        functions.add((toSort) -> {
            toSort.sort(comparator);
            return toSort;
        });
        return this;
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
        Map<K, V> map = new HashMap<>();
        for (T it : doFunctions()) {
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
    @SuppressWarnings("unchecked")
    public Set toSet() {
        return new LinkedHashSet(doFunctions());
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
        return new ArrayList<>(doFunctions());
    }

    @SuppressWarnings("unchecked")
    private List<T> doFunctions() {
        if (functions.isEmpty()) {
            return list;
        } else {
            List<?> result = new ArrayList<>(list);
            for (Function f : functions) {
                result = doFunction(result, f);
            }
            return  (List<T>)result;
        }
    }

    @SuppressWarnings("unchecked")
    private List<?> doFunction(List<?> fList, Function f) {
        return (List<?>) f.apply(fList);
    }
}