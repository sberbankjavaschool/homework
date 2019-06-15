package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}.
 */
public class Streams<T> {

    private final Supplier<List<T>> supplier;


    private Streams(Supplier<List<T>> supplier) {
        this.supplier = supplier;
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SafeVarargs
    public static <T> Streams<T> of(@NonNull T... elements) {
        Supplier<List<T>> supplier = () -> new ArrayList<>(Arrays.asList(elements));
        return new Streams<>(supplier);
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(@NonNull Collection<T> elements) {
        Supplier<List<T>> supplier = () -> new ArrayList<>(elements);
        return new Streams<>(supplier);
    }

    /**
     * Фильтрация элементов по заданному правилу. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     *
     * <p>Аналог Stream#map(Function).
     *
     * <p>ntermediate операция.
     *
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */
    public Streams<T> filter(@NonNull Predicate<? super T> predicate) {
        Supplier<List<T>> result = () -> {
            List<T> list = new ArrayList<>();
            for (T element : supplier.get()) {
                if (predicate.test(element)) {
                    list.add(element);
                }
            }
            return list;
        };
        return new Streams<>(result);
    }

    /**
     * Преобразование элементов в какие-то другие элементы. Необходимо подобрать нужый интерфейс
     * для передачи в этот метод.
     *
     * <p>Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа
     * (если это возможно).
     *
     * <p>Аналог Stream#map.
     *
     * <p>Intermediate операция.
     *
     * @param function - правило траснформации элементов.
     * @return стрим
     */
    public <R> Streams<R> transform(@NonNull Function<? super T, ? extends R> function) {
        Supplier<List<R>> result = () -> {
            List<R> list = new ArrayList<>();
            for (T element : supplier.get()) {
                list.add(function.apply(element));
            }
            return list;
        };
        return new Streams<>(result);
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
        Supplier<List<T>> result = () -> {
            List<T> list = new ArrayList<>(supplier.get());
            list.sort(comparator);
            return list;
        };
        return new Streams<>(result);
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
        for (T element : supplier.get()) {
            result.put(keyMapper.apply(element), valueMapper.apply(element));
        }
        return result;
    }

    /**
     * Преобразование стрима в Set.
     *
     * <p>Данный метод должен уметь преобразовать стрим в Set. Обратите внимание, что метод sorted должен
     * учитываться при вызове этого метода.
     *
     * <p>Terminate операция.
     *
     * @return Set элементов
     */
    public Set<T> toSet() {
        return new LinkedHashSet<>(supplier.get());
    }

    /**
     * Преобразование стрима в List.
     *
     * <p>Данный метод должен уметь преобразовать стрим в List.
     *
     *  <p>Terminate операция.
     *
     * @return List элементов
     */
    public List<T> toList() {
        return new ArrayList<>(supplier.get());
    }

}