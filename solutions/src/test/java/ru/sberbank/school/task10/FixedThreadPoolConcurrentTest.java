package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

class FixedThreadPoolConcurrentTest {

    @Test
    void checkNullThreads() {
        System.out.println("1st test");
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FixedThreadPool(0));
    }

    @Test
    void checkWorkWithLatch() throws InterruptedException {
        System.out.println("2d test");
        CountDownLatch latch = new CountDownLatch(100);
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        fixedThreadPoolConcurrent.start();
        for (int i = 0; i < 100; i++) {
            fixedThreadPoolConcurrent.execute(() -> {
                latch.countDown();
                System.out.println(Thread.currentThread().getName() + " is done ");
            });
        }
        latch.await();
        Assertions.assertEquals(10, fixedThreadPoolConcurrent.getThreadsSize());
    }

    @Test
    void checkCallAfterStop() {
        System.out.println("3d test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        fixedThreadPoolConcurrent.start();
        fixedThreadPoolConcurrent.stopNow();
        Assertions.assertThrows(RuntimeException.class, () -> fixedThreadPoolConcurrent.execute(() -> {
        }));

    }

    @Test
    void checkStopBeforeStart() {
        System.out.println("4th test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        Assertions.assertThrows(IllegalStateException.class, () -> fixedThreadPoolConcurrent.stopNow());

    }

    @Test
    void checkExecuteBeforeStart() {
        System.out.println("5th test");
        FixedThreadPoolConcurrent fixedThreadPoolConcurrent = new FixedThreadPoolConcurrent(10);
        Assertions.assertThrows(IllegalStateException.class, () -> fixedThreadPoolConcurrent.execute(() -> {
        }));

    }

}