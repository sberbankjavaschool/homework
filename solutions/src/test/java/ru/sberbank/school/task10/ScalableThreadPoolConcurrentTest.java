package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

class ScalableThreadPoolConcurrentTest {
    private static ScalableThreadPoolConcurrent scalableThreadPoolConcurrent;

    void start() {
        scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        scalableThreadPoolConcurrent.start();
        for (int i = 0; i < 10; i++) {
            scalableThreadPoolConcurrent.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " is done");
            });
        }
    }

    @Test
    @DisplayName("checkIllegalArgument")
    void checkIllegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ScalableThreadPoolConcurrent(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ScalableThreadPoolConcurrent(2, 1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ScalableThreadPoolConcurrent(-1, 1));
    }

    @Test
    @DisplayName("checkCountThreadsMin")
    void checkCountThreadsMin() throws InterruptedException {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        CountDownLatch latch = new CountDownLatch(22);
        scalableThreadPoolConcurrent.start();
        for (int i = 0; i < 22; i++) {
            Thread.sleep(100);
            scalableThreadPoolConcurrent.execute(() -> {
                latch.countDown();
            });
        }
        latch.await();
        Assertions.assertEquals(5, scalableThreadPoolConcurrent.getThreads().size());
        //System.out.println(scalablePool.getThreads().size());
    }

    @Test
    @DisplayName("checkCountThreadsMax")
    void checkCountThreadsMax() throws InterruptedException {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        CountDownLatch latch = new CountDownLatch(20);
        scalableThreadPoolConcurrent.start();
        for (int i = 0; i < 20; i++) {
            scalableThreadPoolConcurrent.execute(() -> {
                        try {
                            Thread.sleep(1000);
                            latch.countDown();
                            System.out.println(Thread.currentThread().getName() + " is done");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
        latch.await();
        Assertions.assertEquals(10, scalableThreadPoolConcurrent.getThreads().size());
        //System.out.println(scalablePool.getThreads().size());
    }

    @Test
    @DisplayName("checkIsActiveThreads")
    void checkIsActiveThreads() {
        start();
        scalableThreadPoolConcurrent.stopNow();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (scalableThreadPoolConcurrent) {
            boolean checkThreads = scalableThreadPoolConcurrent.checkScalableThreads();
            Assertions.assertEquals(checkThreads, true);
        }
    }

    @Test
    @DisplayName("checkCallAfterStop")
    void checkCallAfterStop() {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        scalableThreadPoolConcurrent.start();
        scalableThreadPoolConcurrent.stopNow();
        synchronized (scalableThreadPoolConcurrent) {
            Assertions.assertThrows(RuntimeException.class, () -> scalableThreadPoolConcurrent.execute(() -> {
            }));
        }
    }

    @Test
    @DisplayName("checkStopBeforeStart")
    void checkStopBeforeStart() {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        synchronized (scalableThreadPoolConcurrent) {
            Assertions.assertThrows(IllegalStateException.class, () -> scalableThreadPoolConcurrent.stopNow());
        }
    }

    @Test
    @DisplayName("checkExecuteBeforeStart")
    void checkExecuteBeforeStart() {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        synchronized (scalableThreadPoolConcurrent) {
            Assertions.assertThrows(IllegalStateException.class,
                    () -> scalableThreadPoolConcurrent.execute(() -> {
                    }));
        }
    }


}