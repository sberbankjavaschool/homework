package ru.sberbank.school.task11;

import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/** Облегченная версия класса {@link Stream}.
 */
@Solution(12)
public class StreamsImpl<T> {
    private final List<T> items;

    private StreamsImpl(List<T> items) {
        this.items = items;
    }

    /** Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SafeVarargs
    public static <T> StreamsImpl<T> of(T... elements) {
        return new StreamsImpl<>(Arrays.asList(elements));
    }

    /** Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> StreamsImpl<T> of(Collection<T> elements) {
        return new StreamsImpl<>(new ArrayList<>(elements));

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
        List<T> resultList = new ArrayList<>();
        for (T element : items) {
            if (object.test(element)) {
                resultList.add(element);
            }
        }
        return new StreamsImpl<>(resultList);
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
        List<R> resultList = new ArrayList<>();
        for (T element : items) {
            resultList.add(object.apply(element));
        }
        return new StreamsImpl<>(resultList);
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
        List<T> sortedList = new ArrayList<>(items);
        sortedList.sort(object);
        return new StreamsImpl<>(sortedList);
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
        for (T element : items) {
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
        return new HashSet<>(items);
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
        return null;
    }

}