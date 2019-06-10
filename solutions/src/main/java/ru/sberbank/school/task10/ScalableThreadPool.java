package ru.sberbank.school.task10;

import ru.sberbank.school.util.Solution;

import java.util.Objects;

@Solution(11)
public class ScalableThreadPool extends FixedThreadPool {
    private final int maxPoolSize;

    public ScalableThreadPool(int minPoolSize, int maxPoolSize) {
        super(minPoolSize);
        if (minPoolSize > maxPoolSize) {
            throw new IllegalArgumentException("Max pool size shouldn't be less than min pool size");
        }
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public void execute(Runnable runnable) {
        Objects.requireNonNull(runnable);
        if (super.threadPoolSize() < maxPoolSize) {
            super.startNewThread(true);
        }
        super.execute(runnable);
    }
}
