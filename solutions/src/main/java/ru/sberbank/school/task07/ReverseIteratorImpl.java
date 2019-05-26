package ru.sberbank.school.task07;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 26.05.2019
 * Реализация простого обратного итератора для обхода списка
 *
 * @author Gregory Melnikov
 */

public class ReverseIteratorImpl<E> implements ReverseOrderIterator<E> {

    private int cursor;

    private final List<E> list;

    public ReverseIteratorImpl(List<E> list) {
        this.list = list;
        cursor = list.size();
    }

    @Override
    public boolean hasNext() {
        return cursor != 0;
    }

    @Override
    public E next() {
        if (cursor < 1) {
            throw new NoSuchElementException();
        }
        return list.get(--cursor);
    }
}


class ReverseOrderIteratorImplSimple<E> implements ReverseOrderIterator<E> {

    private final List<E> list;

    private ListIterator<E> iterator = null;

    public ReverseOrderIteratorImplSimple(List<E> list) {
        this.list = list;
    }

    @PostConstruct
    private void initiateIterator() {
        iterator = list.listIterator(list.size());
    }

    @Override
    public boolean hasNext() {
        return iterator.hasPrevious();
    }

    @Override
    public E next() {
        return iterator.previous();
    }
}