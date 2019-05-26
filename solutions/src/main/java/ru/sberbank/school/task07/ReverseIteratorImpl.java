package ru.sberbank.school.task07;

import java.util.List;
import java.util.Objects;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator {
    private List<E> list;
    private int current;

    public ReverseIteratorImpl(List<E> list) {
        Objects.requireNonNull(list);

        this.list = list;
        this.current = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return current >= 0;
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            throw new NullPointerException("Нечего возвращать!");
        }

        return list.get(current--);
    }
}
