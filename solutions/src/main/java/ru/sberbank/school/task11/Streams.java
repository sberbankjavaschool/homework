package ru.sberbank.school.task11;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
    public static <T> Streams<T> of(T... elements) {
        return new Streams<>(() -> new ArrayList<>(Arrays.asList(elements)));
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(Collection<T> elements) {
        return new Streams<>(() -> new ArrayList<>(elements));
    }

    /**
     * Фильтрация элементов по заданному правилу. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     * Аналог Stream#map(Function).
     * Intermediate операция.
     *
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */
    public Streams<T> filter(Predicate<T> predicate) {
        return new Streams<T>(() -> {
            List<T> result = supplier.get();
            result.removeIf((item) -> !predicate.test(item));
            return result;
        });
    }

    /**
     * Преобразование элементов в какие-то другие элементы.
     * Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     * Аналог Stream#map.
     * Intermediate операция.
     *
     * @param function - правило траснформации элементов.
     * @return стрим
     */
    public <E> Streams<E> transform(Function<? super T, ? extends E> function) {
        return new Streams<E>(() -> {
            List<T> data = supplier.get();
            List<E> result = new ArrayList<>();
            for (T item : data) {
                result.add(function.apply(item));
            }
            return result;
        });
    }

    /**
     * Сортировка элементов. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Данный метод должен уметь сортировать элементы по заданному правилу.
     * Аналог Stream#sorted
     * Intermediate операция.
     *
     * @param comparator - правило сортировки элементов.
     * @return стрим
     */
    public Streams<T> sorted(Comparator<? super T> comparator) {
        return new Streams<>(() -> {
            List<T> result = supplier.get();
            result.sort(comparator);
            return result;
        });
    }

    /**
     * Преобразование стрима в Map. Необходимо подобрать нужные интерфейсы для передачи в этот метод.
     * Данный метод должен уметь преобразовать стрим в Map используя keyMapper и valueMapper.
     * Terminate операция.
     *
     * @param keyMapper   - правило создания ключа.
     * @param valueMapper - правило создания значения.
     * @return Map, собранная по правилам keyMapper и valueMapper
     */
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                           Function<? super T, ? extends V> valueMapper) {
        Map<K, V> result = new HashMap<>();
        List<T> data = supplier.get();
        for (T item : data) {
            result.put(keyMapper.apply(item), valueMapper.apply(item));
        }
        return result;
    }

    /**
     * Преобразование стрима в Set.
     * Данный метод должен уметь преобразовать стрим в Set. Обратите внимание, что метод sorted должен учитываться
     * при вызове этого метода.
     * Terminate операция.
     *
     * @return Set элементов
     */
    public Set<T> toSet() {
        return new LinkedHashSet<>(supplier.get());
    }

    /**
     * Преобразование стрима в List.
     * Данный метод должен уметь преобразовать стрим в List.
     * Terminate операция.
     *
     * @return List элементов
     */
    public List<T> toList() {
        return new ArrayList<>(supplier.get());
    }

}