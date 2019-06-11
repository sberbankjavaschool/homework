package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class FixedThreadPoolTest {

    @Test
    void allThreadsDoTheirWork() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(200);
        FixedThreadPool pool = new FixedThreadPool(10);
        AtomicInteger counter = new AtomicInteger(0);
        pool.start();
        for (int i = 0; i < 200; i++) {
            pool.execute(() -> {
                counter.getAndAdd(1);
                latch.countDown();
            });
        }
        latch.await();
        Assertions.assertEquals(200, counter.get());
    }


    @Test
    void workingTestFuture() throws InterruptedException, ExecutionException {
        CountDownLatch latch = new CountDownLatch(200);
        FixedThreadPool pool = new FixedThreadPool(10);
        pool.start();
        List<Future<String>> results = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            int finalI = i;
            results.add(pool.execute(() -> {
                latch.countDown();
                return " " + finalI + " ";
            }));
        }
        latch.await();
        List<String> expectedList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            expectedList.add(" " + i + " ");
        }
        List<String> list = new ArrayList<>();
        for (Future<String> result : results) {
            String s = result.get();
            list.add(s);
        }
        Assertions.assertEquals(expectedList, list);

    }

    @Test
    void threadPoolInterrupts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(50);
        FixedThreadPool pool = new FixedThreadPool(10);
        AtomicInteger counter = new AtomicInteger(0);
        pool.start();
        for (int i = 0; i < 200; i++) {
            pool.execute(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter.getAndAdd(1);
                latch.countDown();
            });
        }
        latch.await();
        pool.stopNow();
        Assertions.assertAll(() -> {
            Assertions.assertTrue(counter.get() < 100);
            Assertions.assertEquals(0, pool.threadPoolSize());
        });
    }

    @Test
    void threadPoolInterruptedWhenIdle() {
        FixedThreadPool pool = new FixedThreadPool(10);
        pool.start();
        pool.stopNow();
        Assertions.assertEquals(0, pool.threadPoolSize());
    }

    @Test
    void restartsThreadsWhenExceptionHappens() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        FixedThreadPool pool = new FixedThreadPool(5);
        pool.start();
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                latch.countDown();
                throw new RuntimeException();

            });
        }
        latch.await();
        Assertions.assertEquals(5, pool.threadPoolSize());
    }

    @Test
    void startTestDoesNotIncreaseThreadsOverPoolSize() {
        FixedThreadPool pool = new FixedThreadPool(5);
        pool.start();
        pool.startNewThread(false);
        pool.startNewThread(true);
        Assertions.assertEquals(5, pool.threadPoolSize());
    }
}