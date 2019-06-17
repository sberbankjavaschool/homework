package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentScalableThreadPoolTest {
    private static final int MIN = 3;
    private static final int MAX = 7;

    @Test
    @DisplayName("Наличие исключения, при неправильных аргументах конструктора")
    void checkIllegalConstructorArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(3, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(0, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(-5, 3));
    }

    @Test
    @DisplayName("Наличие исключения, при попытке остановки до старта")
    void checkCantStoppedBeforeStart() {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        Assertions.assertThrows(IllegalStateException.class, threadPool::stopNow);
    }

    @Test
    @DisplayName("Наличие исключения, при попытке добавить задачу до старта")
    void cantExecuteBeforeStarted() {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        AtomicInteger count = new AtomicInteger(0);
        Assertions.assertThrows(IllegalStateException.class,
                () -> threadPool.execute(count::incrementAndGet));
    }

    @Test
    @DisplayName("Тест работы после повторного запуска")
    void checkWorkabilityAfterStopped() throws InterruptedException {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        AtomicInteger count = new AtomicInteger(0);
        threadPool.start();
        threadPool.stopNow();
        threadPool.start();

        CountDownLatch latch = new CountDownLatch(50);

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                count.incrementAndGet();
                latch.countDown();
            });
        }

        latch.await(1, TimeUnit.SECONDS);
        threadPool.stopNow();

        Assertions.assertEquals(20, count.get());
    }

    @Test
    @DisplayName("Наличие исключения, при попытке дважды запустить тердпул")
    void checkDoubleStart() {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        threadPool.start();
        Assertions.assertThrows(IllegalStateException.class, threadPool::start);
    }

    @Test
    @DisplayName("Совпадение количества запущенных и выполнных задач")
    void checkCountOfExecutedTask() throws InterruptedException {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        AtomicInteger count = new AtomicInteger(0);
        threadPool.start();
        CountDownLatch latch = new CountDownLatch(50);

        for (int i = 0; i < 50; i++) {
            threadPool.execute(() -> {
                count.incrementAndGet();
                latch.countDown();
            });
        }

        latch.await();
        threadPool.stopNow();
        Assertions.assertEquals(50, count.get());
    }

    @Test
    @DisplayName("При отсутвии задач, ThreadPool не расширяется")
    void checkMinThreadCount() throws InterruptedException {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        threadPool.start();
        CountDownLatch latch = new CountDownLatch(15);

        for (int i = 0; i < 20; i++) {
            threadPool.execute(latch::countDown);
            TimeUnit.MILLISECONDS.sleep(10);
        }

        latch.await();
        Assertions.assertEquals(MIN, threadPool.getThreadCount());
        threadPool.stopNow();
    }

    @Test
    @DisplayName("При наличии задач, ThreadPool расширяется до максимального")
    void checkMaxThreadCount() throws InterruptedException {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        AtomicInteger count = new AtomicInteger(0);
        threadPool.start();
        CountDownLatch latch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                count.incrementAndGet();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException ignore) {
                    //ignore
                }

                latch.countDown();
            });
        }

        latch.await();
        Assertions.assertEquals(MAX, threadPool.getThreadCount());
        threadPool.stopNow();
    }

    @Test
    @DisplayName("ThreadPool может уменьшится до минимального, после уменьшения нагрузки")
    void checkDectimentThreadCount() throws InterruptedException {
        ConcurrentScalableThreadPool threadPool = new ConcurrentScalableThreadPool(MIN, MAX);
        AtomicInteger count = new AtomicInteger(0);
        threadPool.start();
        CountDownLatch latch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> {
                count.incrementAndGet();
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException ignore) {
                    //ignore
                }

                latch.countDown();
            });
        }

        latch.await();
        Assertions.assertEquals(MAX, threadPool.getThreadCount());

        CountDownLatch latch2 = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            threadPool.execute(latch2::countDown);
            TimeUnit.MILLISECONDS.sleep(10);
        }

        latch2.await();
        Assertions.assertEquals(MIN, threadPool.getThreadCount());
        threadPool.stopNow();
    }
}
