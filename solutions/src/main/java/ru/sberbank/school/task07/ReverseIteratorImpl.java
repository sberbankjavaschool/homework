package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.List;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator<E> {

    private final List<E> elements;
    private int currentIndex;

    public ReverseIteratorImpl(@NonNull List<E> elements) {
        this.elements = elements;
        currentIndex = elements.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return currentIndex >= 0;
    }

    @Override
    public E next() {
        return elements.get(currentIndex--);
    }
}
