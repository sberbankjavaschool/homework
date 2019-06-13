package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}
 */
public class StreamsImpl<T> {
    private final List<T> myList = new ArrayList<>();
    private final List<Function> functions = new ArrayList<>();


    private StreamsImpl(@NonNull T... elements) {
        myList.addAll(Arrays.asList(elements));
    }

    private StreamsImpl(@NonNull Collection<? extends T> elements) {
        myList.addAll(elements);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */

    public static <T> StreamsImpl<T> of(@NonNull T... elements) {
        return new StreamsImpl(elements);
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */

    public static <T> StreamsImpl<T> of(@NonNull Collection<T> elements) {
        return new StreamsImpl(elements);
    }

    /**
     * Фильтрация элементов по заданному правилу. Необходимо подобрать нужный интерфейс для передачи в этот метод.
     * Иными словами, этот метод оставляет в коллекции только те элементы, которые удовлетворяют условию в лямбде.
     * Аналог Stream#map(Function).
     * Intermediate операция.
     *
     * @param predicate - правило фильтрации элементов.
     * @return стрим
     */

    public StreamsImpl<T> filter(@NonNull Predicate<? super T> predicate) {
        Function function = (f) -> {
            List<T> tList = new ArrayList<>();
            for (T elem : myList) {
                if (predicate.test(elem)) {
                    tList.add(elem);
                }
            }
            return tList;
        };
        functions.add(function);
        return this;
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

    public <R> StreamsImpl<T> transform(@NonNull Function<? super T, ? extends R> function) {
        Function myFunction = (f) -> {
            List<R> tList = new ArrayList<>();
            for (T elem : myList) {
                tList.add(function.apply(elem));
            }
            return tList;
        };
        functions.add(myFunction);
        return this;
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

    public StreamsImpl<T> sorted(@NonNull Comparator<? super T> comparator) {
        Function myFunction = (f) -> {
            List<T> tList = new ArrayList<>(myList);
            tList.sort(comparator);
            return tList;
        };
        functions.add(myFunction);
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
        List<T> tList = applyFunctions();
        Map<K, V> map = new HashMap<>();
        for (T elem : tList) {
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
        return new LinkedHashSet<>(applyFunctions());
    }

    /**
     * Преобразование стрима в List.
     * Данный метод должен уметь преобразовать стрим в List.
     * Terminate операция.
     *
     * @return List элементов
     */
    public List<T> toList() {
        return new ArrayList(applyFunctions());
    }

    private List<T> applyFunctions() {
        List<T> tList = new ArrayList<>();
        for (Function f : functions) {
            tList = (List<T>) f.apply(myList);
        }
        return tList;
    }

}