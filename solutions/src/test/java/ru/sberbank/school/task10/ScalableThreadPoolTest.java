package ru.sberbank.school.task10;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPoolTest {

    @Test
    public void incorrectThreadNumberTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ScalableThreadPool(0, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ScalableThreadPool(3, 1));
    }

    @Test
    public void interruptTest() throws InterruptedException {
        Runnable task = () -> Assertions.assertThrows(InterruptedException.class, () -> Thread.sleep(10000));
        ScalableThreadPool fixedThreadPool = new ScalableThreadPool(1, 2);
        fixedThreadPool.start();
        fixedThreadPool.execute(task);
        Thread.sleep(1000);
        fixedThreadPool.stopNow();
    }

    @Test
    @DisplayName("Тест вызова метода execute до start")
    public void testRunExecuteBeforeStart() {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2,4);
        Assertions.assertThrows(IllegalStateException.class, () -> scalableThreadPool.execute(() -> 1));
    }

    @Test
    @DisplayName("Тест вызова метода stopNow до start")
    public void testRunStopBeforeStart() {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2,4);
        Assertions.assertThrows(IllegalStateException.class, scalableThreadPool::stopNow);
    }

    @Test
    public void testDoubleCallsStart() {
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(2,4);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            scalableThreadPool.start();
            scalableThreadPool.start();
        });
        scalableThreadPool.stopNow();
    }

    @Test
    public void threadCollectorTest() throws InterruptedException {
        Runnable zeroThread = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable firstThread = () -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(1, 2);
        scalableThreadPool.start();
        scalableThreadPool.execute(zeroThread);
        scalableThreadPool.execute(firstThread);
        Thread.sleep(6000);
        Assertions.assertEquals(1, scalableThreadPool.getWorkingThreadsCount());
        scalableThreadPool.stopNow();
    }

    @Test
    public void testNumberOfCompletedTasks() throws ExecutionException, InterruptedException {
        final int countThreads = 10;
        AtomicInteger result = new AtomicInteger(0);
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(5,10);
        scalableThreadPool.start();
        Callable<Integer> callable = () -> 1;
        List<Future<Integer>> futures = new ArrayList<>(countThreads);
        for (int i = 0; i < countThreads; i++) {
            futures.add(scalableThreadPool.execute(callable));
        }
        Thread.sleep(4000);
        for (Future<Integer> f : futures) {
            result.addAndGet(f.get());
        }
        scalableThreadPool.stopNow();
        Assertions.assertEquals(result.get(), countThreads);
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
        ScalableThreadPool scalableThreadPool = new ScalableThreadPool(1,1);
        scalableThreadPool.start();
        Future<Integer> future = scalableThreadPool.execute(callable);
        int result = future.get();
        Assertions.assertEquals(result, expectedValue);
        scalableThreadPool.stopNow();
    }
}
