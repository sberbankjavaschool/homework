package ru.sberbank.school.task10;

public class FixedThreadPool extends ScalableThreadPool {
    public FixedThreadPool(int n) {
        super(n, n);
    }
}