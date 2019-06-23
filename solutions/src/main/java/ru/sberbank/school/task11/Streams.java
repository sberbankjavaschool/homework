package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}
 */
public class Streams<T> {

    private final Supplier<List<T>> supplier;

    public Streams(Supplier<List<T>> supplier) {
        this.supplier = supplier;
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static<T> Streams<T> of(@NonNull T... elements) {
        return new Streams<>(() -> new ArrayList<>(Arrays.asList(elements)));
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static<T> Streams<T> of(@NonNull Collection<T> elements) {
        return new Streams<>(() -> new ArrayList<>(elements));
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
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */
    public Streams<T> filter(@NonNull Predicate<? super T> predicate) {
        return new Streams<>(() -> {
            List<T> list = new ArrayList<>();
            for (T t : supplier.get()) {
                if (predicate.test(t)) {
                    list.add(t);
                }
            }
            return list;
        });
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
    public <R> Streams<R> transform(@NonNull Function<? super T, ? extends R> function) {
        return new Streams<>(() -> {
            List<R> list = new ArrayList<>();
            for (T t : supplier.get()) {
                list.add(function.apply(t));
            }
            return list;
        });
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
        return new Streams<>(() -> {
            List<T> list = supplier.get();
            list.sort(comparator);
            return list;
        });
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
    public <K, V> Map toMap(@NonNull Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        Map<K, V> map = new HashMap<>();
        for (T t : supplier.get()) {
            map.put(keyMapper.apply(t), valueMapper.apply(t));
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
    public Set<T> toSet() {
        return new LinkedHashSet<>(supplier.get());
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
    public List<T> toList() {
        return new ArrayList<>(supplier.get());
    }
}