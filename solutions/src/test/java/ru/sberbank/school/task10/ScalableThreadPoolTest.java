package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ScalableThreadPoolTest {

    @Test
    void assertThatTemporaryThreadsWasCreatedAndTerminated() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(120);
        ScalableThreadPool pool = new ScalableThreadPool(3, 10);
        pool.start();
        for (int i = 0; i < 120; i++) {
            TimeUnit.MILLISECONDS.sleep((long) (10 * Math.log(i)));
            pool.execute(() -> {
                try {
                    int timeout = new Random().nextInt(3);
                    TimeUnit.SECONDS.sleep(timeout);
                    latch.countDown();
                } catch (InterruptedException ignored) {}
            });
        }
        latch.await();
        Queue<Integer> expected = new LinkedList<>(Arrays.asList(3, 4, 5, 6, 7, 8, 9));

        Assertions.assertAll(() -> {
            Assertions.assertTrue(expected.containsAll(pool.getThreadNumbers()));
            Assertions.assertTrue(pool.getThreadNumbers().containsAll(expected));
        });
    }

    @Test
    void throwsExceptionWithWrongSizeArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ScalableThreadPool(20, 10));
    }

}