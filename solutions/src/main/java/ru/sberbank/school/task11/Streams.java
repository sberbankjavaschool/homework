package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Облегченная версия класса {@link Stream}
 */
public class Streams<T> {

    private final List<Supplier<List<T>>> suppliers = new ArrayList<>();
    private List<T> elements = new ArrayList<>();

    private Streams(Collection<T> elements) {
        this.elements.addAll(elements);
    }

    /**
     * Принимает на вход массив элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(@NonNull T... elements) {
        return new Streams<>(new ArrayList<>(Arrays.asList(elements)));
    }

    /**
     * Принимает на вход коллекцию элементов и возвращает стрим построенный на основе этих элементов.
     *
     * @param elements входные элементы
     * @return стрим элементов из elements
     */
    public static <T> Streams<T> of(@NonNull Collection<T> elements) {
        return new Streams<>(new ArrayList<>(elements));
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
    public Streams<T> filter(Predicate<? super T> object) {
        suppliers.add(() -> elements.stream()
                .filter(object)
                .collect(Collectors.toList()));
        return this;
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
    @SuppressWarnings("unchecked")
    public <R> Streams<R> transform(Function<? super T, ? extends R> object) {

        List<R> temp = new ArrayList<>();

        suppliers.add(() -> {
            temp.addAll(elements.stream()
                    .map(object)
                    .collect(Collectors.toList()));
            return (List<T>) temp;
        });

        return (Streams<R>) this;
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
    public Streams<T> sorted(Comparator<? super T>  object) {
        suppliers.add(() -> elements.stream()
                .sorted(object)
                .collect(Collectors.toList()));
        return this;
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
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper,
                                  Function<? super T, ? extends V> valueMapper) {
        return terminate().stream().collect(Collectors.toMap(keyMapper, valueMapper));
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
        return new LinkedHashSet<>(terminate());
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
        return new ArrayList<>(terminate());
    }

    private List<T> terminate() {
        for (Supplier<List<T>> supplier : suppliers) {
            elements = supplier.get();
        }
        return elements;
    }
}