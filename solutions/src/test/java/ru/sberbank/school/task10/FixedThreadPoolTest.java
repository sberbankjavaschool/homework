package ru.sberbank.school.task10;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPoolTest {
    private ThreadPool threadPool;

    @Before
    public void init() {
        threadPool = new FixedThreadPool(5);
        threadPool.start();
    }

    @Test(timeout = 150)
    public void normalWork() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 25; i++) {
            threadPool.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException ignore) {
//                        e.printStackTrace();
                }
                synchronized (counter) {
                    System.out.println("Task: " + counter.incrementAndGet()
                            + " - Thread: " + Thread.currentThread().getName());
                }
            });
        }
        TimeUnit.MILLISECONDS.sleep(50);
        Assertions.assertEquals(25, counter.get());
        threadPool.stopNow();
    }

    @Test(timeout = 750)
    public void callableService() throws InterruptedException, ExecutionException {
        final List<Future> futureList = new ArrayList<>();
        final AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 25; i++) {
            Future<String> future = threadPool.execute(() -> {
                TimeUnit.MILLISECONDS.sleep(100);
                String res;
                synchronized (counter) {
                    res = "Task: " + counter.incrementAndGet() + " - Thread: " + Thread.currentThread().getName();
                }
                return res + " " + LocalDateTime.now().toString();
            });
            futureList.add(future);
        }
        for (Future future : futureList) {
            System.out.println(future.get());
        }
        Assertions.assertEquals(25, counter.get());
        threadPool.stopNow();
    }

    @Test(expected = IllegalStateException.class)
    public void executeAfterStop() {
        threadPool.stopNow();
        threadPool.execute(() -> {
        });
    }

    @Test(expected = IllegalStateException.class)
    public void doubleStop() {
        threadPool.stopNow();
        threadPool.stopNow();
    }

    @Test(expected = IllegalStateException.class)
    public void doubleStart() {
        threadPool.start();
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongThreadsCount() {
        threadPool = new FixedThreadPool(0);
        threadPool = new FixedThreadPool(-18);
    }


}
