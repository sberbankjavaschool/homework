package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Облегченная версия класса. {@link Stream}
 */
public class MyStreams<T> {

    private final List<T> elements;
    private final List<Function<List<T>, List<?>>> functions = new ArrayList<>();

    @SafeVarargs
    private MyStreams(T...elements) {
        this.elements = Arrays.asList(elements);
    }

    private MyStreams(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    @SafeVarargs
    public static <T> MyStreams<T> of(@NonNull T... elements) {
        return new MyStreams<>(elements);
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> MyStreams<T> of(@NonNull Collection<T> elements) {
        return new MyStreams<>(elements);
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
    public MyStreams<T> filter(@NonNull Predicate<? super T> predicate) {

        functions.add((elements) -> {
            List<T> list = new ArrayList<>();

            for (T element : elements) {
                if (predicate.test(element)) {
                    list.add(element);
                }
            }

            return list;
        });

        return this;
    }

    /**
     * Преобразование элементов в какие-то другие элементы. Необходимо подобрать нужый интерфейс для передачи в этот метод.
     *
     * <p>Например, данный метод должен уметь извлекать какие-то поля из объектов исходного типа (если это возможно).
     *
     * <p>Аналог Stream#map.
     *
     * <p>Intermediate операция.
     *
     * @param function - правило траснформации элементов.
     * @return стрим
     */


    public <R> MyStreams<R> transform(@NonNull Function<? super T, ? extends R> function) {

        functions.add((elements) -> {
            List<R> list = new ArrayList<>(elements.size());

            for (T element : elements) {
                list.add(function.apply(element));
            }

            return list;
        });

        return (MyStreams<R>) this;
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
    public MyStreams<T> sorted(@NonNull Comparator<? super T> comparator) {

        functions.add((elements) -> {
            elements.sort(comparator);
            return elements;
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
        List<T> list = execute();
        Map<K, V> map = new HashMap<>();

        for (T element : list) {
            map.put(keyMapper.apply(element), valueMapper.apply(element));
        }

        return map;
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
        return new LinkedHashSet<>(execute());
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
        return new ArrayList<>(execute());
    }

    private List<T> execute() {
        List<T> result = elements;

        for (Function function : functions) {
            result = (List<T>) function.apply(result);
        }

        return result;
    }

}