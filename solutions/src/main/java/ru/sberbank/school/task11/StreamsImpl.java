package ru.sberbank.school.task11;

import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/** Облегченная версия класса {@link Stream}.
 */
@Solution(12)
public class StreamsImpl<T> {
    private final Supplier<List<T>> supplier;

    private StreamsImpl(Supplier<List<T>> supplier) {
        this.supplier = supplier;
    }

    /** Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SafeVarargs
    public static <T> StreamsImpl<T> of(T... elements) {
        Supplier<List<T>> supplier = () -> Arrays.asList(elements);
        return new StreamsImpl<>(supplier);
    }

    /** Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> StreamsImpl<T> of(Collection<T> elements) {
        Supplier<List<T>> supplier = () -> new ArrayList<>(elements);
        return new StreamsImpl<>(supplier);

    }

    /** Фильтрация элементов по заданному правилу. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     *
     * <p>Аналог Stream#map(Function).
     *
     * <p>Intermediate операция.
     *
     * @param object - правило фильтрации элементов.
     * @return стрим
     */
    public StreamsImpl<T> filter(Predicate<? super T> object) {
        Supplier<List<T>> newSupplier = () -> {
            List<T> resultList = new ArrayList<>();
            for (T element : supplier.get()) {
                if (object.test(element)) {
                    resultList.add(element);
                }
            }
            return resultList;
        };
        return new StreamsImpl<>(newSupplier);
    }

    /** Преобразование элементов в какие-то другие элементы.
     * Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     *
     * <p>Аналог Stream#map.
     *
     * <p>Intermediate операция.
     *
     * @param object - правило траснформации элементов.
     * @return стрим
     */
    public <R> StreamsImpl<R> transform(Function<? super T, ? extends R> object) {
        Supplier<List<R>> newSupplier = () -> {
            List<R> resultList = new ArrayList<>();
            for (T element : supplier.get()) {
                resultList.add(object.apply(element));
            }
            return resultList;
        };
        return new StreamsImpl<>(newSupplier);
    }

    /** Сортировка элементов. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Данный метод должен уметь сортировать элементы по заданному правилу.
     *
     * <p>Аналог Stream#sorted
     *
     * <p>Intermediate операция.
     *
     * @param object - правило сортировки элементов.
     * @return стрим
     */
    public StreamsImpl<T> sorted(Comparator<? super T> object) {
        Supplier<List<T>> newSupplier = () -> {
            List<T> sortedList = new ArrayList<>(supplier.get());
            sortedList.sort(object);
            return sortedList;
        };
        return new StreamsImpl<>(newSupplier);
    }

    /** Преобразование стрима в Map. Необходимо подобрать нужные интерфейсы для передачи в этот метод.
     *
     * <p>Данный метод должен уметь преобразовать стрим в Map используя keyMapper и valueMapper.
     *
     * <p>Terminate операция.
     *
     * @param keyMapper   - правило создания ключа.
     * @param valueMapper - правило создания значения.
     * @return Map, собранная по правилам keyMapper и valueMapper
     */
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends V> valueMapper) {
        Map<K, V> map = new HashMap<>();
        for (T element : supplier.get()) {
            map.put(keyMapper.apply(element), valueMapper.apply(element));
        }
        return map;
    }

    /** Преобразование стрима в Set.
     *
     * <p>Данный метод должен уметь преобразовать стрим в Set. Обратите внимание, что метод sorted должен учитываться
     * при вызове этого метода.
     *
     * <p>Terminate операция.
     *
     * @return Set элементов
     */
    public Set<T> toSet() {
        return new LinkedHashSet<>(supplier.get());
    }

    /** Преобразование стрима в List.
     *
     * <p>Данный метод должен уметь преобразовать стрим в List.
     *
     * <p>Terminate операция.
     *
     * @return List элементов
     */
    public List<T> toList() {
        return new ArrayList<>(supplier.get());
    }

    @Override
    public String toString() {
        return "StreamsImpl{" + "items=" + supplier.get() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StreamsImpl<?> streams = (StreamsImpl<?>) o;
        return supplier.get().equals(streams.supplier.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplier.get());
    }
}