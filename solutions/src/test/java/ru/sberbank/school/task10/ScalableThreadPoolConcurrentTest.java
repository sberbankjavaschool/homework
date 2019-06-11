package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

class ScalableThreadPoolConcurrentTest {

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
        Assertions.assertEquals(5, scalableThreadPoolConcurrent.getThreadsSize());
    }

    @Test
    @DisplayName("checkCountThreadsMax")
    void checkCountThreadsMax() throws InterruptedException {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        CountDownLatch latch = new CountDownLatch(20);
        scalableThreadPoolConcurrent.start();
        for (int i = 0; i < 20; i++) {
            scalableThreadPoolConcurrent.execute(() -> {
                        latch.countDown();
                        System.out.println(Thread.currentThread().getName() + " is done");
                    }
            );
        }
        latch.await();
        Assertions.assertEquals(10, scalableThreadPoolConcurrent.getThreadsSize());
    }

    @Test
    @DisplayName("checkCallAfterStop")
    void checkCallAfterStop() {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        scalableThreadPoolConcurrent.start();
        scalableThreadPoolConcurrent.stopNow();
        Assertions.assertThrows(RuntimeException.class, () -> scalableThreadPoolConcurrent.execute(() -> {
        }));

    }

    @Test
    @DisplayName("checkStopBeforeStart")
    void checkStopBeforeStart() {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        Assertions.assertThrows(IllegalStateException.class, () -> scalableThreadPoolConcurrent.stopNow());
    }

    @Test
    @DisplayName("checkExecuteBeforeStart")
    void checkExecuteBeforeStart() {
        ScalableThreadPoolConcurrent scalableThreadPoolConcurrent = new ScalableThreadPoolConcurrent(5, 10);
        Assertions.assertThrows(IllegalStateException.class,
                () -> scalableThreadPoolConcurrent.execute(() -> {
                }));

    }


}