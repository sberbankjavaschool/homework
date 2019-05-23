package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.List;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator {

    private List<E> list;
    private int currentIndex;

    public ReverseIteratorImpl(@NonNull List<E> list) {
        this.list = list;
        currentIndex = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return currentIndex > -1;
    }

    @Override
    public E next() {
        return list.get(currentIndex--);
    }

}
