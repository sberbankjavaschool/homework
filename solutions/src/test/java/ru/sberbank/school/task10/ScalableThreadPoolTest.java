package ru.sberbank.school.task10;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class ScalableThreadPoolTest {

    @Test
    void workingTestRunnable() throws InterruptedException {
        ScalableThreadPool pool = new ScalableThreadPool(10, 20);
        pool.start();
        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                try {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is working");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.SECONDS.sleep(15);
    }

    @Test
    void workingTestFuture() throws InterruptedException, ExecutionException {
        ScalableThreadPool pool = new ScalableThreadPool(10, 20);
        pool.start();
        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Callable<String> callable = () -> {
                TimeUnit.SECONDS.sleep(1);
                return "Thread " + Thread.currentThread().getName() + " is working";
            };
            results.add(pool.execute(callable));
        }
        for (Future<String> future : results) {
            System.out.println(future.get());
        }
    }

    @Test
    void workingTestInterrupts() throws InterruptedException {
        ScalableThreadPool pool = new ScalableThreadPool(10, 20);
        pool.start();
        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                try {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is working");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        TimeUnit.SECONDS.sleep(3);
        pool.stopNow();
    }
}