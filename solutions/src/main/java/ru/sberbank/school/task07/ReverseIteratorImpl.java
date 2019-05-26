package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.List;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator<E> {

    private List<E> list;
    private int current;

    public ReverseIteratorImpl(@NonNull List<E> list) {
        this.list = list;
        current = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return current >= 0;
    }

    @Override
    public E next() {
        return list.get(current--);
    }
}
