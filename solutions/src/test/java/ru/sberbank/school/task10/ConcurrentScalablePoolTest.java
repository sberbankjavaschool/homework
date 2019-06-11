package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;


class ConcurrentScalablePoolTest {

    @Test
    @DisplayName("Тест на выброс исключения при исполнении задачи без запуска пула")
    void withoutStart() {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);

        Assertions.assertThrows(IllegalStateException.class, () -> scalablePool.execute(() -> {
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
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();
        Assertions.assertThrows(IllegalStateException.class, scalablePool::start);
    }

    @Test
    @DisplayName("Тест на выброс исключения при некорректном колличестве потоков")
    void illegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalablePool(5, 2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalablePool(0, 5));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalablePool(-4, 0));
    }

    @Test
    @DisplayName("Тест на проверку состояния пула при остановке")
    void stopPool() {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();
        scalablePool.stopNow();

        Assertions.assertEquals(0, scalablePool.getTasksCount());
        Assertions.assertEquals(0, scalablePool.getThreadsCount());
    }

    @Test
    @DisplayName("Тест на выброс исключения при исполнении задачи после остановки пула")
    void withoutStartAfterStopping() {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();
        scalablePool.stopNow();
        Assertions.assertThrows(IllegalStateException.class, () -> scalablePool.execute(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        ));
    }

    @Test
    @DisplayName("Тест на проверку состояния пула при повторном запуске пула после остановки")
    void restartAfterStopping() {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();
        scalablePool.stopNow();
        scalablePool.start();

        Assertions.assertEquals(0, scalablePool.getTasksCount());
        Assertions.assertEquals(2, scalablePool.getThreadsCount());
    }

    @Test
    @DisplayName("Тест на остановку пула без его запуска")
    void stoppingWithoutStarting() {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        Assertions.assertThrows(IllegalStateException.class, scalablePool::stopNow);
    }

    @Test
    @DisplayName("Тест на повторную остановку пула")
    void doubleStopping() {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();
        scalablePool.stopNow();
        Assertions.assertThrows(IllegalStateException.class, scalablePool::stopNow);
    }

    @Test
    @DisplayName("Тест на устойчивость пула к исключениям в задачах")
    void exceptionInRunnable() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();

        for (int i = 0; i < 15; i++) {
            scalablePool.execute(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
                System.out.println(5 / 0);
            });
        }

        latch.await();
        Assertions.assertEquals(6, scalablePool.getThreadsCount());
    }

    @Test
    @DisplayName("Тест на колличество исполняющихся потоков в пуле без нагрузки")
    void countRunningThreads() throws InterruptedException {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        CountDownLatch latch = new CountDownLatch(15);
        scalablePool.start();

        for (int i = 0; i < 15; i++) {
            scalablePool.execute(latch::countDown);

            Thread.sleep(10);
        }

        latch.await();

        Assertions.assertEquals(2, scalablePool.getThreadsCount());
    }

    @Test
    @DisplayName("Тест на колличество исполняющихся потоков в пуле с нагрузкой")
    void countRunningThreads2() throws InterruptedException {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        CountDownLatch latch = new CountDownLatch(15);
        scalablePool.start();

        for (int i = 0; i < 15; i++) {
            scalablePool.execute(() -> {
                        try {
                            Thread.sleep(500);
                            latch.countDown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }

        latch.await();

        Assertions.assertEquals(6, scalablePool.getThreadsCount());
    }

    @Test
    @DisplayName("Тест на колличество исполняющихся потоков в пуле с переменной нагрузкой")
    void countRunningThreads3() throws InterruptedException {
        ConcurrentScalablePool scalablePool = new ConcurrentScalablePool(2, 6);
        scalablePool.start();
        final CountDownLatch latch = new CountDownLatch(15);

        for (int i = 0; i < 15; i++) {
            scalablePool.execute(() -> {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();
        Assertions.assertEquals(6, scalablePool.getThreadsCount());

        for (int i = 0; i < 15; i++) {
            Thread.sleep(20);

            scalablePool.execute(() -> System.out.println("Hello") );
        }

        Assertions.assertEquals(2, scalablePool.getThreadsCount());
        CountDownLatch latch3 = new CountDownLatch(15);

        for (int i = 0; i < 15; i++) {
            scalablePool.execute(() -> {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            latch3.countDown();
                        }
                    }
            );
        }

        latch3.await();
        Assertions.assertEquals(6, scalablePool.getThreadsCount());
    }

}