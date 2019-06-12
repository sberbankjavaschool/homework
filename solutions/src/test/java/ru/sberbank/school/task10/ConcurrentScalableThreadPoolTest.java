package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ConcurrentScalableThreadPoolTest {

    @Test
    public void incorrectThreadNumberTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(0, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(3, 1));
    }

    @Test
    public void interruptTest() {
        Runnable task = () -> Assertions.assertThrows(InterruptedException.class, () -> TimeUnit.SECONDS.sleep(10));
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(1, 2);
        try {
            scalableThreadPool.start();
            scalableThreadPool.execute(task);
        } finally {
            scalableThreadPool.stopNow();
        }
    }

    @Test
    @DisplayName("Тест вызова метода execute до start")
    public void testRunExecuteBeforeStart() {
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(2,4);
        Assertions.assertThrows(IllegalStateException.class, () -> scalableThreadPool.execute(() -> 1));
    }

    @Test
    @DisplayName("Тест вызова метода stopNow до start")
    public void testRunStopBeforeStart() {
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(2,4);
        Assertions.assertThrows(IllegalStateException.class, scalableThreadPool::stopNow);
    }

    @Test
    public void testDoubleCallsStart() {
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(2,4);
        try {
            Assertions.assertThrows(IllegalStateException.class, () -> {
                scalableThreadPool.start();
                scalableThreadPool.start();
            });
        } finally {
            scalableThreadPool.stopNow();
        }
    }

    @Test
    public void threadCollectorTest() throws InterruptedException {
        final int corePoolSize = 10;
        final int poolCount = 30;
        CountDownLatch countDownLatch = new CountDownLatch(poolCount);
        Runnable runnable = countDownLatch::countDown;
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(corePoolSize, 20);
        try {
            scalableThreadPool.start();
            for (int i = 0; i < poolCount; i++) {
                scalableThreadPool.execute(runnable);
            }
            countDownLatch.await();
            Assertions.assertEquals(corePoolSize, scalableThreadPool.getWorkingThreadsCount());
        } finally {
            scalableThreadPool.stopNow();
        }
    }

    @Test
    public void testNumberOfCompletedTasks() throws ExecutionException, InterruptedException {
        final int numberRunnable = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(numberRunnable);
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(5, 10);
        Runnable runnable = countDownLatch::countDown;
        try {
            scalableThreadPool.start();
            for (int i = 0; i < numberRunnable; i++) {
                scalableThreadPool.execute(runnable);
            }
            countDownLatch.await();
            Assertions.assertEquals(0, countDownLatch.getCount());
        } finally {
            scalableThreadPool.stopNow();
        }
    }

    @Test
    public void testCorrectCompletionWork() throws InterruptedException, ExecutionException {
        final int expectedValue = 100;
        Callable<Integer> callable = () -> {
            int count = 0;
            for (int i = 0; i < expectedValue; i++) {
                count++;
            }
            return count;
        };
        ConcurrentScalableThreadPool scalableThreadPool = new ConcurrentScalableThreadPool(1,1);
        try {
            scalableThreadPool.start();
            Future<Integer> future = scalableThreadPool.execute(callable);
            int result = future.get();
            Assertions.assertEquals(result, expectedValue);
        } finally {
            scalableThreadPool.stopNow();
        }
    }
}
