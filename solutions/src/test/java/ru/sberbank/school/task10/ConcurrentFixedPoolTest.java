package ru.sberbank.school.task10;


import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

class ConcurrentFixedPoolTest {

    @Test
    @DisplayName("Тест на выброс исключения при исполнении задачи без запуска пула")
    void withoutStart() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);

        Assertions.assertThrows(IllegalStateException.class, () -> fixedPool.execute(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        ));

    }

    @Test
    @DisplayName("Тест на выброс исключения при повторном запуске пула")
    void restart() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();
        Assertions.assertThrows(IllegalStateException.class, fixedPool::start);
    }

    @Test
    @DisplayName("Тест на выброс исключения при колличестве потоков меньше 1")
    void illegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedPool(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedPool(-1));
    }

    @Test
    @DisplayName("Тест на проверку состояния пула при остановке")
    void stopPool() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();
        fixedPool.stopNow();
        Optional<Thread> first = Arrays.stream(fixedPool.getThreads()).filter(Objects::nonNull).findFirst();

        Assertions.assertFalse(fixedPool.isExist());
        Assertions.assertEquals(0, fixedPool.getTasksCount());
        Assertions.assertFalse(first.isPresent());
    }

    @Test
    @DisplayName("Тест на выброс исключения при исполнении задачи после остановки пула")
    void withoutStartAfterStopping() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();
        fixedPool.stopNow();
        Assertions.assertThrows(IllegalStateException.class, () -> fixedPool.execute(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        ));
    }

    @Test
    @DisplayName("Тест на устойчивость пула к исключениям в задачах")
    void exceptionInRunnable() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();

        for (int i = 0; i < 10; i++) {
            fixedPool.execute((Runnable) () -> {
                latch.countDown();
                throw new NullPointerException();
            });
        }

        latch.await();
        Assertions.assertEquals(3, fixedPool.getThreads().length);
    }

    @Test
    @DisplayName("Тест на проверку состояния пула при повторном запуске пула после остановки")
    void restartAfterStopping() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();
        fixedPool.stopNow();
        fixedPool.start();
        long countThreads = Arrays.stream(fixedPool.getThreads()).filter(Objects::nonNull).count();

        Assertions.assertTrue(fixedPool.isExist());
        Assertions.assertEquals(0, fixedPool.getTasksCount());
        Assertions.assertEquals(fixedPool.getThreads().length, countThreads);
    }

    @Test
    @DisplayName("Тест на остановку пула без его запуска")
    void stoppingWithoutStarting() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        Assertions.assertThrows(IllegalStateException.class, fixedPool::stopNow);
    }

    @Test
    @DisplayName("Тест на повторную остановку пула")
    void doubleStopping() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();
        fixedPool.stopNow();
        Assertions.assertThrows(IllegalStateException.class, fixedPool::stopNow);
    }

    @Test
    @DisplayName("Тест на колличество исполняющихся потоков в пуле")
    void countRunningThreads() {
        ConcurrentFixedPool fixedPool = new ConcurrentFixedPool(3);
        fixedPool.start();

        for (int i = 0; i < 10; i++) {
            fixedPool.execute(() -> {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        Assertions.assertEquals(3, fixedPool.getThreads().length);

    }
}