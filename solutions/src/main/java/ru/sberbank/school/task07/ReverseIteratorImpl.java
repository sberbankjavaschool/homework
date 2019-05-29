package ru.sberbank.school.task07;

import java.util.List;

public class ReverseIteratorImpl<T> implements ReverseOrderIterator<T> {

    private List<T> collection;
    private int pointer;

    public ReverseIteratorImpl(List<T> collection) {
        this.collection = collection;
        pointer = collection.size();
    }

    @Override
    public boolean hasNext() {
        return pointer != 0;
    }

    @Override
    public T next() {
        pointer--;
        return collection.get(pointer);
    }
}
