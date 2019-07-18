package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.List;
import java.util.NoSuchElementException;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator {

    private List<E> list;
    private int cursor;

    public ReverseIteratorImpl(@NonNull List<E> list) {
        this.list = list;
        this.cursor = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return cursor >= 0;
    }

    @Override
    public E next() {
        if (cursor < 0) {
            throw new NoSuchElementException();
        }
        return list.get(cursor--);
    }
}