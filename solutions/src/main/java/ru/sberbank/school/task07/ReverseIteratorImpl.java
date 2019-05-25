package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.*;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator {
    private int cursor;
    private int lastRet = -1;

    private List<E> list;

    public ReverseIteratorImpl(@NonNull List<E> list) {
        this.list = new ArrayList<>(list);
        cursor = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return cursor >= 0;
    }

    @Override
    public E next() {
        int i = cursor;
        if (i < 0) {
            throw new NoSuchElementException();
        }
        cursor = i - 1;
        return list.get(lastRet = i);
    }

    @Override
    public void remove() {
        if (lastRet < 0) {
            throw new IllegalStateException();
        }
        list.remove(lastRet);
        lastRet = -1;
    }
}
