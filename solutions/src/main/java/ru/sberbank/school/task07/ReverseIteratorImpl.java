package ru.sberbank.school.task07;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Задание: Реализуйте свой Iterator для обхода списка в обратном порядке.
 */
public class ReverseIteratorImpl<E> implements ReverseOrderIterator {
    private int pointer;
    private List<E> elements;

    public ReverseIteratorImpl(@NonNull List<E> elements) {
        this.elements = new ArrayList<>(elements);
        this.pointer = this.elements.size() - 1;
    }

    @Override
    public boolean hasNext() {
        if (pointer > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public E next() throws IndexOutOfBoundsException {
        if (this.hasNext()) {
            pointer--;
            return elements.get(pointer);
        } else {
            throw new IndexOutOfBoundsException("End of list");
        }
    }
}
