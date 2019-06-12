package ru.sberbank.school.task10;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentScalableThreadPoolTest {
    private ConcurrentScalableThreadPool pool;
    private int minSizePool = 5;
    private int maxSizePool = 10;

    private void start() {
        pool = new ConcurrentScalableThreadPool(minSizePool, maxSizePool);
        pool.start();
    }

    private void stop() {
        pool.stopNow();
        waitStoppedPool();
    }

    private void waitStoppedPool() {
        int count = -1;
        while (count != 0) {
            Set<Thread> threads = Thread.getAllStackTraces().keySet();
            int checkCount = 0;
            for (Thread thread : threads) {
                if (thread.getName().contains("ThreadPoolWorker")) {
                    checkCount++;
                }
            }
            count = checkCount;
        }
    }

    @Test
    @DisplayName("Тест создания пула с некорректным размером")
    public void testIncorrectSizePool() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(0, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentScalableThreadPool(5, 3));
    }

    @Test
    @DisplayName("Проверка двойного запуска")
    public void testDoubleStart() {
        start();
        Assertions.assertThrows(IllegalStateException.class, () -> pool.start());
        stop();
    }

    @Test
    @DisplayName("Проверка выполнения всех поставленных runnable задач")
    public void testExecuteRunnableTasks() throws InterruptedException {
        start();
        int amount = 100;
        CountDownLatch latch = new CountDownLatch(amount);
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();

        for (int i = 0; i < amount; i++) {
            pool.execute(() -> {
                queue.add(Thread.currentThread().getName());
                latch.countDown();
            });
        }

        latch.await();
        stop();
        Assert.assertEquals(queue.size(), amount);
    }

    @Test
    @DisplayName("Проверка выполнения всех поставленных callable задач")
    public void testExecuteCallableTasks() throws ExecutionException, InterruptedException {
        start();
        int amount = 100;
        int count = 0;

        for (int i = 0; i < amount; i++) {
            Future<Integer> future = pool.execute(() -> 1);
            count += future.get();
        }
        stop();
        Assert.assertEquals(count, amount);

    }

    @Test
    @DisplayName("Проверка количества рабочих тредов при отсутствие задач")
    public void testCountRunnableThreadWithoutTasks() {
        start();

        int checkCount = 0;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            if (thread.getName().contains("ThreadPoolWorker")) {
                checkCount++;
            }
        }
        stop();

        Assert.assertEquals(checkCount, minSizePool);
    }

    @Test
    @DisplayName("Проверка количества рабочих тредов при высокой нагрузке пула")
    public void testCountRunnableThreadAtHighLoad() {
        start();
        pool.execute(() -> System.out.println("Тест количества тредов при высокой нагрузке"));
        int checkCount = 0;
        for (int i = 0; i < 20; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            if (i == 15) {
                Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                for (Thread thread : threadSet) {
                    if (thread.getName().contains("ThreadPoolWorker") && !thread.isInterrupted()) {
                        checkCount++;
                    }
                }
            }
        }

        stop();
        Assert.assertTrue(checkCount > minSizePool && checkCount <= maxSizePool);

    }

    @Test
    @DisplayName("Проверка сужения пула после расширения, если нет задач в очереди")
    public void testConstrictionPoolIfNotTask() throws ExecutionException, InterruptedException {
        start();

        int countTask = 10;
        CountDownLatch latch = new CountDownLatch(countTask);

        for (int i = 0; i < countTask; i++) {
            pool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Future<Integer> future = pool.execute(() -> {
            Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
            int checkCount = 0;
            for (Thread thread : threadSet) {
                if (thread.getName().contains("ThreadPoolWorker") && !thread.isInterrupted()) {
                    checkCount++;
                }
            }
            return checkCount;
        });
        int checkCountThread = future.get();
        stop();

        Assert.assertEquals(checkCountThread, minSizePool);
    }


    @Test
    @DisplayName("Проверка двойной остановки")
    public void testDoubleStop() {
        start();
        stop();
        Assertions.assertThrows(IllegalStateException.class, this::stop);
    }


    @Test
    @DisplayName("Тест попытки добавить задачу в очередь остановленного пула")
    public void testMethodExecuteStoppedPool() {
        start();
        stop();
        Assertions.assertThrows(IllegalStateException.class, () -> pool.execute(() -> System.out.println("test")));
    }

    @Test
    @DisplayName("Тест остановки пула")
    public void testStopPool() {
        start();
        stop();

        int checkCount = 0;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            if (thread.getName().contains("ThreadPoolWorker")) {
                checkCount++;
            }
        }

        Assert.assertEquals(checkCount, 0);
    }
}
