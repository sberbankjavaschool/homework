package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class FixedThreadPoolConcurrentTest {
    private static FixedThreadPoolConcurrent fixedThreadPool;

    void start() {
        fixedThreadPool = new FixedThreadPoolConcurrent(10);
        fixedThreadPool.start();
        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is done");
            });
        }
    }

    @Test
    @DisplayName("checkWithNullThreads")
    void checkNullThreads() {
        System.out.println("1st test");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FixedThreadPool(0));
    }

    @Test
    @DisplayName("checkWorkWithLatch")
    void checkWorkWithLatch() throws InterruptedException {
        System.out.println("2d test");
        CountDownLatch latch = new CountDownLatch(100);
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        AtomicInteger counter = new AtomicInteger(0);
        fixedThreadPoolConcurrent.start();
        for (int i = 0; i < 100; i++) {
            fixedThreadPoolConcurrent.execute(() -> {
                counter.addAndGet(1);
                latch.countDown();
                System.out.println(Thread.currentThread().getName() + " is done");
            });
        }
        latch.await();
        Assertions.assertEquals(100, counter.get());
    }

    @Test
    @DisplayName("checkCountThreads")
    void checkCountThreads() {
        System.out.println("3d test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        fixedThreadPoolConcurrent.start();
        for (int i = 0; i < 10; i++) {
            fixedThreadPoolConcurrent.execute(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        Assertions.assertEquals(10, fixedThreadPoolConcurrent.getThreads().size());
    }

    @Test
    @DisplayName("checkIsActiveThreads")
    void checkIsActiveThreads() {
        System.out.println("4th test");
        start();
        fixedThreadPool.stopNow();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (fixedThreadPool) {
            boolean checkThreads = fixedThreadPool.checkThreads();
            Assertions.assertEquals(checkThreads, true);
        }
    }

    @Test
    @DisplayName("checkCallAfterStop")
    void checkCallAfterStop() {
        System.out.println("5th test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        fixedThreadPoolConcurrent.start();
        fixedThreadPoolConcurrent.stopNow();
        synchronized (fixedThreadPoolConcurrent) {
            Assertions.assertThrows(RuntimeException.class, () -> fixedThreadPoolConcurrent.execute(() -> {
            }));
        }
    }

    @Test
    @DisplayName("checkStopBeforeStart")
    void checkStopBeforeStart() {
        System.out.println("6th test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        synchronized (fixedThreadPoolConcurrent) {
            Assertions.assertThrows(IllegalStateException.class, () -> fixedThreadPoolConcurrent.stopNow());
        }
    }

    @Test
    @DisplayName("checkExecuteBeforeStart")
    void checkExecuteBeforeStart() {
        System.out.println("7th test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        synchronized (fixedThreadPoolConcurrent) {
            Assertions.assertThrows(IllegalStateException.class, () -> fixedThreadPoolConcurrent.execute(() -> {
            }));
        }
    }

}