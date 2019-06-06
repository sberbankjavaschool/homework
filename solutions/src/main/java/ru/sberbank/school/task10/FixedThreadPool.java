package ru.sberbank.school.task10;

public class FixedThreadPool extends ScalableThreadPool implements ThreadPool {

    public FixedThreadPool(int amount) {
        super(amount, amount);
    }
}
