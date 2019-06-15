package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Streams<T> {
    private final List<T> items;
    private final List<Function<List<T>, List<?>>> functions = new ArrayList<>();

    private Streams(@NonNull Collection<T> stream) {
        this.items = new ArrayList<>(stream);
    }

    @SafeVarargs
    private Streams(@NonNull T ... args) {
        this.items = Arrays.asList(args);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SafeVarargs
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
     *
     * <p>Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     *
     * <p>Аналог Stream#map(Function).
     *
     * <p>Intermediate операция.
     *
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */
    public Streams<T> filter(@NonNull Predicate<? super T> predicate) {
        functions.add((items) -> {
            List<T> result = new ArrayList<>();

            for (T item : items) {
                if (predicate.test(item)) {
                    result.add(item);
                }
            }

            return result;
        });

        return this;
    }

    /**
     * Преобразование элементов в какие-то другие элементы.
     * Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     *
     * <p>Аналог Stream#map.
     *
     * <p>Intermediate операция.
     *
     * @param transformer - правило траснформации элементов.
     * @return стрим
     */
    @SuppressWarnings("unchecked")
    public <R> Streams<R> transform(@NonNull Function<? super T, ? extends R> transformer) {
        functions.add((items) -> {
            List<R> result = new ArrayList<>();
            for (T item : items) {
                result.add(transformer.apply(item));
            }

            return result;
        });

        return (Streams<R>) this;
    }

    /**
     * Сортировка элементов. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Данный метод должен уметь сортировать элементы по заданному правилу.
     *
     * <p>Аналог Stream#sorted
     *
     * <p>Intermediate операция.
     *
     * @param comparator - правило сортировки элементов.
     * @return стрим
     */
    public Streams<T> sorted(@NonNull Comparator<? super T> comparator) {
        functions.add((items) -> {
            List<T> result = new ArrayList<>(items);
            result.sort(comparator);

            return result;
        });

        return this;
    }

    /**
     * Преобразование стрима в Map. Необходимо подобрать нужные интерфейсы для передачи в этот метод.
     *
     * <p>Данный метод должен уметь преобразовать стрим в Map используя keyMapper и valueMapper.
     *
     * <p>Terminate операция.
     *
     * @param keyMapper   - правило создания ключа.
     * @param valueMapper - правило создания значения.
     * @return Map, собранная по правилам keyMapper и valueMapper
     */
    public <K, V> Map<K, V> toMap(@NonNull Function<? super T, ? extends K> keyMapper,
                                  @NonNull Function<? super T, ? extends V> valueMapper) {
        Map<K, V> result = new HashMap<>();

        for (T item : apply()) {
            result.put(keyMapper.apply(item), valueMapper.apply(item));
        }

        return result;
    }

    /**
     * Преобразование стрима в Set.
     *
     * <p>Данный метод должен уметь преобразовать стрим в Set. Обратите внимание, что метод sorted должен учитываться
     * при вызове этого метода.
     *
     * <p>Terminate операция.
     *
     * @return Set элементов
     */
    public Set<T> toSet() {
        return new LinkedHashSet<>(apply());
    }

    /**
     * Преобразование стрима в List.
     *
     * <p>Данный метод должен уметь преобразовать стрим в List.
     *
     * <p>Terminate операция.
     *
     * @return List элементов
     */
    public List<T> toList() {
        return apply();
    }

    @SuppressWarnings("unchecked")
    private List<T> apply() {
        List<T> result = items;

        for (Function func : functions) {
            result = (List<T>) func.apply(result);
        }

        return result;
    }

}