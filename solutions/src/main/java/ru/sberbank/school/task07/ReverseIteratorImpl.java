package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.List;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator {

    private List<E> list;
    private int index;


    public ReverseIteratorImpl(@NonNull List<E> list) {
        this.list = list;
        index = list.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return index > -1;
    }

    @Override
    public E next() {
        return list.get(index --);
    }
}
