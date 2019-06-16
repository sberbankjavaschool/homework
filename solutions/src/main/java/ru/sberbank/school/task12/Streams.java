package ru.sberbank.school.task12;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 16.06.2019
 * Облегченная версия класса {@link Stream}
 */
@Solution(12)
public class Streams<T> {

    private final Collection<? extends T> source;

    private Streams(Collection<? extends T> elements) {
        this.source = Collections.unmodifiableList(new ArrayList<>(elements));
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SuppressWarnings("unchecked")
    public static <T> Streams<T> of(@NonNull T... elements) {
        return new Streams<>(Arrays.asList(elements));
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SuppressWarnings("unchecked")
    public static <T> Streams<T> of(@NonNull Collection<? extends T> elements) {
        return new Streams<>(elements);
    }

    /**
     * Фильтрация элементов по заданному правилу. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     * Аналог Stream#filter(Function).
     * Intermediate операция.
     *
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */
    public Streams<T> filter(@NonNull Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T t : source) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
        return new Streams<>(result);
    }

    /**
     * Преобразование элементов в какие-то другие элементы.
     * Необходимо подобрать нужый интерфейс для передачи в этот метод.
     * Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     * Аналог Stream#map.
     * Intermediate операция.
     *
     * @param mapper - правило траснформации элементов.
     * @return стрим
     */
    public <R> Streams<R> transform(@NonNull Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T t : source) {
            result.add(mapper.apply(t));
        }
        return new Streams<>(result);
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
    public Streams<T> sorted(@NonNull Comparator<? super T> comparator) {
        List<T> result = new ArrayList<>(source);
        result.sort(comparator);
        return new Streams<>(result);
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
    public <K, V> Map<K, V> toMap(@NonNull Function<? super T, ? extends K> keyMapper,
                                  @NonNull Function<? super T, ? extends V> valueMapper) {
        Map<K, V> map = new HashMap<>();
        for (T t : source) {
            map.put(keyMapper.apply(t), valueMapper.apply(t));
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
        return new LinkedHashSet<>(source);
    }

    /**
     * Преобразование стрима в List.
     * Данный метод должен уметь преобразовать стрим в List.
     * Terminate операция.
     *
     * @return List элементов
     */
    public List<T> toList() {
        return new ArrayList<>(source);
    }
}