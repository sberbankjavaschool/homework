package ru.sberbank.school.task07;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Created by Mart
 * 01.06.2019
 **/
public class ReverseIteratorImpl<E> implements ReverseOrderIterator {
    private List<E> list;
    private int index;

    public ReverseIteratorImpl(List<E> list) {
        Objects.requireNonNull(list, "Iterating list cannot be null");

        this.list = list;
        this.index = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return index >= 0;
    }

    @Override
    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return list.get(index--);
    }
}
