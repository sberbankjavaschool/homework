package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ReverseIteratorImpl<E> implements ReverseOrderIterator<E> {
    private List<E> list;
    private int currentIndex;

    public ReverseIteratorImpl(@NonNull List<E> newList) {
        list = new ArrayList<>();
        list.addAll(newList);
        currentIndex = newList.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return (currentIndex >= 0);
    }

    @Override
    public E next() {
        return list.get(currentIndex--);
    }
}
