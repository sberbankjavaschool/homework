package ru.sberbank.school.task10;

public class ConcurrentFixedThreadPool extends ConcurrentScalableThreadPool {

    public ConcurrentFixedThreadPool(int amount) {
        super(amount, amount);
    }
}
