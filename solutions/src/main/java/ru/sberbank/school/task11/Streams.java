package ru.sberbank.school.task11;

import lombok.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Streams<T> {

    private final Generator<T> generatorFunction;

    private Streams(Generator<T> generator) {
        this.generatorFunction = generator;
    }

    public static <T> Streams<T> of(T... elements) {
        return new Streams<>(generatorContext -> {
            for (T e : elements) {
                generatorContext.accept(e);
            }
        });
    }

    public static <T> Streams<T> of(@NonNull Collection<T> elements) {
        return new Streams<>(elements::forEach);
    }

    public Streams<T> filter(@NonNull Predicate<? super T> predicate) {
        return new Streams<>(generatorContext -> generatorFunction.generate(value -> {
            if (predicate.test(value)) {
                generatorContext.accept(value);
            }
        }));
    }

    public <R> Streams<R> transform(@NonNull Function<? super T, ? extends R> function) {
        return new Streams<>(generatorContext ->
                generatorFunction.generate(value ->
                        generatorContext.accept(function.apply(value))));
    }

    public Streams<T> sorted(@NonNull Comparator<? super T> comparator) {
        TreeSet<T> set = new TreeSet<>(comparator);
        generatorFunction.generate(set::add);
        return new Streams<>(of(set).generatorFunction);
    }

    public <K, V> Map<K, V> toMap(@NonNull Function<? super T, ? extends K> keyMapper,
                                  @NonNull Function<? super T, ? extends V> valueMapper) {
        HashMap<K, V> map = new HashMap<>();
        generatorFunction.generate(value ->
                map.put(keyMapper.apply(value), valueMapper.apply(value)));
        return map;
    }

    public Set<T> toSet() {
        Set<T> set = new LinkedHashSet<>();
        generatorFunction.generate(set::add);
        return set;
    }

    public List<T> toList() {
        List<T> list = new ArrayList<>();
        generatorFunction.generate(list::add);
        return list;
    }

    public interface Generator<T> {
        void generate(Consumer<T> context);
    }
}