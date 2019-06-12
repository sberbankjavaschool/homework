package ru.sberbank.school.task10;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcurrentFixedThreadPoolTest {
    private int core = 5;
    private ThreadPool threadPool;

    @Before
    public void init() {
        threadPool = new ConcurrentFixedThreadPool(core);
    }

    private Runnable newRunnable(AtomicInteger counter, CountDownLatch latch) {
        return () -> {
            System.out.println("Task: " + counter.incrementAndGet()
                    + " - Thread: " + Thread.currentThread().getName());

            latch.countDown();
        };
    }

    private Callable<String> newCallable(AtomicInteger counter, CountDownLatch latch) {
        return () -> {
            String res;
            res = "Task: " + counter.incrementAndGet() + " - Thread: " + Thread.currentThread().getName();

            latch.countDown();
            return res + " " + LocalDateTime.now().toString();
        };
    }

    private int getThreadCount() throws InterruptedException {
        // пауза, чтобы потоки успели создаться/прерваться до того, как я их посчитаю
        TimeUnit.MILLISECONDS.sleep(10);
        return Thread.getAllStackTraces().keySet().size();
    }

    @Test
    public void normalWork() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger(0);
        final int initialThreadSize = getThreadCount();
        final int iterations = 100;
        int nowThreadSize;

        threadPool.start();
        nowThreadSize = getThreadCount();
        assertEquals(core, nowThreadSize - initialThreadSize);

        CountDownLatch latch = new CountDownLatch(iterations);
        for (int i = 0; i < iterations; i++) {
            threadPool.execute(newRunnable(counter, latch));
        }
        latch.await();
        assertEquals(iterations, counter.get());

        nowThreadSize = getThreadCount();
        assertEquals(core, nowThreadSize - initialThreadSize);

        threadPool.stopNow();
        nowThreadSize = getThreadCount();
        assertEquals(nowThreadSize, initialThreadSize);
    }

    @Test
    public void callableService() throws InterruptedException, ExecutionException {
        final List<Future<String>> futureList = new ArrayList<>();
        final AtomicInteger counter = new AtomicInteger(0);
        final int initialThreadSize = getThreadCount();
        final int iterations = 100;
        int nowThreadSize;

        threadPool.start();
        nowThreadSize = getThreadCount();
        assertEquals(core, nowThreadSize - initialThreadSize);

        CountDownLatch latch = new CountDownLatch(iterations);
        for (int i = 0; i < iterations; i++) {
            Future<String> future = threadPool.execute(newCallable(counter, latch));
            futureList.add(future);
        }
        for (Future future : futureList) {
            System.out.println(future.get());
        }
        latch.await();
        assertEquals(iterations, counter.get());

        nowThreadSize = getThreadCount();
        assertEquals(core, nowThreadSize - initialThreadSize);

        threadPool.stopNow();
        nowThreadSize = getThreadCount();
        assertEquals(nowThreadSize, initialThreadSize);
    }

    @Test
    public void executeAfterStop() {
        threadPool.start();
        threadPool.stopNow();
        Assertions.assertThrows(IllegalStateException.class, () ->
                threadPool.execute(() -> {
                }));
    }

    @Test
    public void doubleStop() {
        threadPool.start();
        threadPool.stopNow();
        Assertions.assertThrows(IllegalStateException.class, () -> threadPool.stopNow());
    }

    @Test
    public void doubleStart() {
        threadPool.start();
        Assertions.assertThrows(IllegalStateException.class, () -> threadPool.start());

    }

    @Test
    public void wrongThreadsCount() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class,
                        () -> new ConcurrentFixedThreadPool(0)),
                () -> Assertions.assertThrows(IllegalArgumentException.class,
                        () -> new ConcurrentFixedThreadPool(-18))
        );
    }
}

