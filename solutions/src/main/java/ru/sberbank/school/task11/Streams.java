package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * Облегченная версия класса {@link Stream}.
 *
 * @param <T> the type of elements
 */
public class Streams<T> {

    private final List<Supplier<List<T>>> operations = new ArrayList<>();
    private List<T> elements;


    private Streams(Collection<? extends T> elements) {
        this.elements = new ArrayList<>(elements);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SafeVarargs
    public static <T> Streams<T> of(@NonNull T... elements) {
        return new Streams<>(Arrays.asList(elements));
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(@NonNull Collection<? extends T> elements) {
        return new Streams<>(elements);
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
    public Streams<T> filter(@NonNull Predicate<? super T> predicate) {

        operations.add(() -> {
            List<T> list = new ArrayList<>();
            for (T elem : elements) {
                if (predicate.test(elem)) {
                    list.add(elem);
                }
            }
            return list;
        });

        return this;
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
        operations.add(() -> {
            List<R> list = new ArrayList<>();
            for (T elem : elements) {
                list.add(mapper.apply(elem));
            }
            return (List<T>) list;
        });
        return (Streams<R>) this;
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

        operations.add(() -> {
            List<T> list = new ArrayList<>(this.elements);
            list.sort(comparator);
            return list;
        });

        return this;
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
        List<T> list = executeOperations();
        Map<K, V> map = new LinkedHashMap<>();
        for (T elem : list) {
            map.put(keyMapper.apply(elem), valueMapper.apply(elem));
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
        return new LinkedHashSet<>(executeOperations());
    }

    /**
     * Преобразование стрима в List.
     * Данный метод должен уметь преобразовать стрим в List.
     * Terminate операция.
     *
     * @return List элементов
     */
    public  List<T> toList() {
        return new ArrayList<>(executeOperations());
    }

    private List<T> executeOperations() {

        if (operations.size() == 0) {
            return new ArrayList<>(elements);

        }
        for (Supplier<List<T>> operation : operations) {
            elements = operation.get();
        }
        return elements;
    }

}