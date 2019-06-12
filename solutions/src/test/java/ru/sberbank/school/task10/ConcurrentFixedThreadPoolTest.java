package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class ConcurrentFixedThreadPoolTest {

    @Test
    public void incorrectThreadNumberTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedThreadPool(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedThreadPool(-1));
    }

    @Test
    public void interruptTest() {
        Runnable task = () -> Assertions.assertThrows(InterruptedException.class, () -> TimeUnit.SECONDS.sleep(10));
        ConcurrentFixedThreadPool fixedThreadPool = new ConcurrentFixedThreadPool(1);
        fixedThreadPool.start();
        fixedThreadPool.execute(task);
        fixedThreadPool.stopNow();
    }

    @Test
    public void testNumberOfCompletedTasks() throws InterruptedException {
        final int numberRunnable = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(numberRunnable);
        ConcurrentFixedThreadPool fixedThreadPool = new ConcurrentFixedThreadPool(5);
        Runnable runnable = countDownLatch::countDown;
        fixedThreadPool.start();
        for (int i = 0; i < numberRunnable; i++) {
            fixedThreadPool.execute(runnable);
        }
        countDownLatch.await();
        fixedThreadPool.stopNow();
        Assertions.assertEquals(0, countDownLatch.getCount());

    }

    @Test
    @DisplayName("Тест вызова метода execute до start")
    public void testRunExecuteBeforeStart() {
        ConcurrentFixedThreadPool fixedThreadPool = new ConcurrentFixedThreadPool(1);
        Assertions.assertThrows(IllegalStateException.class, () -> fixedThreadPool.execute(() -> 1));
    }

    @Test
    @DisplayName("Тест вызова метода stopNow до start")
    public void testRunStopBeforeStart() {
        ConcurrentFixedThreadPool fixedThreadPool = new ConcurrentFixedThreadPool(1);
        Assertions.assertThrows(IllegalStateException.class, fixedThreadPool::stopNow);
    }

    @Test
    public void testDoubleCallsStart() {
        ConcurrentFixedThreadPool fixedThreadPool = new ConcurrentFixedThreadPool(1);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            fixedThreadPool.start();
            fixedThreadPool.start();
        });
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
        ConcurrentFixedThreadPool fixedThreadPool = new ConcurrentFixedThreadPool(1);
        fixedThreadPool.start();
        Future<Integer> future = fixedThreadPool.execute(callable);
        int result = future.get();
        Assertions.assertEquals(result, expectedValue);
        fixedThreadPool.stopNow();
    }
}
