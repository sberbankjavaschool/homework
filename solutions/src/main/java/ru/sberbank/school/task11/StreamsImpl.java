package ru.sberbank.school.task11;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StreamsImpl<T> {
    private final Supplier<List<T>> supplier;

    private StreamsImpl(Supplier<List<T>> supplier) {
        this.supplier = supplier;

    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> StreamsImpl<T> of(T... elements) {
        return new StreamsImpl<>(() -> new ArrayList<>(Arrays.asList(elements)));
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> StreamsImpl<T> of(Collection<? extends T> elements) {
        return new StreamsImpl<>(() -> new ArrayList<>(elements));
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
    public StreamsImpl<T> filter(Predicate<T> predicate) {
        return new StreamsImpl<>(() -> {
            List<T> result = new ArrayList<>(supplier.get());
            result.removeIf((x) -> !predicate.test(x));
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
    public <R> StreamsImpl<R> transform(Function<? super T, ? extends R> function) {

        return new StreamsImpl<>(() -> {
            List<T> elements = supplier.get();
            List<R> result = new ArrayList<>();
            for (T element : elements) {
                result.add(function.apply(element));
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
    public StreamsImpl<T> sorted(Comparator<? super T> comparator) {

        return new StreamsImpl<>(() -> {
            List<T> result = new ArrayList<>(supplier.get());
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

        Map<K, V> map = new HashMap<>();
        for (T element : supplier.get()) {
            map.put(keyMapper.apply(element), valueMapper.apply(element));
        }
        return map;
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
     * Данный метод должен уметь преобразовать стрим в List.
     * Terminate операция.
     *
     * @return List элементов
     */

    public List<T> toList() {
        return new ArrayList<>(supplier.get());
    }
}