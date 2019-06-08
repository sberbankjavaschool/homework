package ru.sberbank.school.task11;

import ru.sberbank.school.util.Solution;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}
 */
@Solution(12)
public class StreamsImpl<T> {

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> StreamsImpl<T> of(T... elements) {
        return null;
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> StreamsImpl<T> of(Collection<T> elements) {
        return null;
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
    public StreamsImpl<T> filter(Predicate<? super T> object) {
        return null;
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
     * @param object - правило траснформации элементов.
     * @return стрим
     */
    public <R> StreamsImpl<R> transform(Function<? super T, ? extends R> object) {
        return null;
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
     * @param object - правило сортировки элементов.
     * @return стрим
     */
    public StreamsImpl<T> sorted(Comparator<? super T> object) {
        return null;
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
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        return null;
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
        return null;
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
        return null;
    }

}