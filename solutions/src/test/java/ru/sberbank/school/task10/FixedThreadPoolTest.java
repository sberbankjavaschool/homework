package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPoolTest {

    @Test
    public void incorrectThreadNumberTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedThreadPool(0));
    }

    @Test
    public void interruptTest() throws InterruptedException {
        Runnable task = () -> Assertions.assertThrows(InterruptedException.class, () -> Thread.sleep(10000));
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);
        fixedThreadPool.start();
        fixedThreadPool.execute(task);
        Thread.sleep(1000);
        fixedThreadPool.stopNow();
    }

    @Test
    public void testNumberOfCompletedTasks() throws ExecutionException, InterruptedException {
        final int countThreads = 10;
        int result = 0;
        FixedThreadPool fixedThreadPool = new FixedThreadPool(5);
        fixedThreadPool.start();
        Callable<Integer> callable = () -> 1;
        for (int i = 0; i < countThreads; i++) {
            Future<Integer> future = fixedThreadPool.execute(callable);
            result += future.get();
        }
        fixedThreadPool.stopNow();
        Assertions.assertEquals(result, countThreads);

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
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);
        fixedThreadPool.start();
        Future<Integer> future = fixedThreadPool.execute(callable);
        int result = future.get();
        Assertions.assertEquals(result, expectedValue);
    }

    @Test
    @DisplayName("Тест вызова метода execute до start")
    public void testRunExecuteBeforeStart() {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);
        Assertions.assertThrows(IllegalStateException.class, () -> fixedThreadPool.execute(() -> 1));
    }

    @Test
    @DisplayName("Тест вызова метода stopNow до start")
    public void testRunStopBeforeStart() {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);
        Assertions.assertThrows(IllegalStateException.class, fixedThreadPool::stopNow);
    }

    @Test
    public void testDoubleCallsStart() {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            fixedThreadPool.start();
            fixedThreadPool.start();
        });
    }
}
