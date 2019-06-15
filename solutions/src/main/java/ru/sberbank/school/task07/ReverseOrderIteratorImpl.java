package ru.sberbank.school.task07;


import java.util.List;

public class ReverseOrderIteratorImpl<E> implements ReverseOrderIterator<E> {

    private int cursor;
    private final List<E> list;

    public ReverseOrderIteratorImpl(List<E> list) {
        this.cursor = list.size() - 1;
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return cursor > 0;
    }

    @Override
    public E next() {
        E e = null;
        try {
            e = list.get(cursor);
            cursor--;
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return e;
    }
}
