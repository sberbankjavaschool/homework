package ru.sberbank.school.task10;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;
import java.util.concurrent.*;

public class ConcurrentFixedThreadPoolTest {
    private int countThread = 10;
    private ConcurrentFixedThreadPool pool;

    private void start() {
        pool = new ConcurrentFixedThreadPool(countThread);
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
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedThreadPool(0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ConcurrentFixedThreadPool(-1));
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
        Assert.assertEquals(queue.size(), amount);
        stop();
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

        Assert.assertEquals(count, amount);
        stop();
    }

    @Test
    @DisplayName("Проверка количества рабочих тредов")
    public void testCountRunnableThread() {
        start();
        int checkCount = 0;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread thread : threadSet) {
            if (thread.getName().contains("ThreadPoolWorker")) {
                checkCount++;
            }
        }

        Assert.assertEquals(checkCount, countThread);
        stop();
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