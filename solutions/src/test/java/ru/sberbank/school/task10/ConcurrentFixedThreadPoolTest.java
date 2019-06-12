package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentFixedThreadPoolTest {
    private static final int COUNT = 3;

    @Test
    @DisplayName("Наличие исключения, при неправильных аргументах конструктора")
    void checkIllegalConstructorArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedThreadPool(-1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedThreadPool(0));;
    }

    @Test
    @DisplayName("Наличие исключения, при попытке остановки до старта")
    void checkCantStoppedBeforeStart() {
        ConcurrentFixedThreadPool threadPool = new ConcurrentFixedThreadPool(COUNT);
        Assertions.assertThrows(IllegalStateException.class, threadPool::stopNow);
    }

    @Test
    @DisplayName("Наличие исключения, при попытке дважды запустить тердпул")
    void checkDoubleStart() {
        ConcurrentFixedThreadPool threadPool = new ConcurrentFixedThreadPool(COUNT);
        threadPool.start();
        Assertions.assertThrows(IllegalStateException.class, threadPool::start);
    }

    @Test
    @DisplayName("Наличие исключения, при попытке добавить задачу до старта")
    void cantExecuteBeforeStarted() {
        ConcurrentFixedThreadPool threadPool = new ConcurrentFixedThreadPool(COUNT);
        AtomicInteger count = new AtomicInteger(0);
        Assertions.assertThrows(IllegalStateException.class,
                () -> threadPool.execute(count::incrementAndGet));
    }

    @Test
    @DisplayName("Тест работы после повторного запуска")
    void checkWorkabilityAfterStopped() throws InterruptedException {
        ConcurrentFixedThreadPool threadPool = new ConcurrentFixedThreadPool(COUNT);
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
    @DisplayName("Совпадение количества запущенных и выполнных задач")
    void checkCountOfExecutedTask() throws InterruptedException {
        ConcurrentFixedThreadPool threadPool = new ConcurrentFixedThreadPool(COUNT);
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



}
