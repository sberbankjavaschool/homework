package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.List;
import java.util.NoSuchElementException;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator {

    private List<E> listIter;
    private int cursor;

    ReverseIteratorImpl(@NonNull List<E> listIter) {
        this.listIter = listIter;
        cursor = listIter.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return  cursor != 0;
    }

    @Override
    public E next() {
        if (cursor == -1) {
            throw new NoSuchElementException();
        }
        return listIter.get(cursor--);
    }
}
