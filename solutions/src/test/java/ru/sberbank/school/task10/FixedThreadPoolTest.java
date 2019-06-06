package ru.sberbank.school.task10;

import org.junit.jupiter.api.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPoolTest {
    static ThreadPool threadPool;
    static AtomicInteger count;

    @BeforeEach
    public void init() {
        threadPool = new FixedThreadPool(2);
        threadPool.start();
        count = new AtomicInteger(0);
    }

    @AfterEach
    public void stop() {
        threadPool.stopNow();
    }

    @Test
    public void checkIllegalConstructorArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedThreadPool(0));
    }

    @Test
    public void checkCountOfExecutedTask() {
        for (int i = 0; i < 20; i++) {
            threadPool.execute((Runnable) count::incrementAndGet);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignore) {
            //ignore
        }

        Assertions.assertEquals(20, count.get());
    }

    @Test
    public void checkCountOfExecutedTaskCallable() {
        for (int i = 0; i < 20; i++) {
            threadPool.execute((Callable<? extends Object>) count::incrementAndGet);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignore) {
            //ignore
        }

        Assertions.assertEquals(20, count.get());
    }

    @Test void checkWorkabilityAfterStopped() {
        threadPool.stopNow();
        threadPool.start();

        for (int i = 0; i < 20; i++) {
            threadPool.execute((Runnable) count::incrementAndGet);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException ignore) {
            //ignore
        }

        Assertions.assertEquals(20, count.get());
    }
}