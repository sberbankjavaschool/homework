package ru.sberbank.school.task10;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FixedThreadPoolTest {
    private FixedThreadPool pool;
    private int amount = 100;
    private volatile int count = 0;
    private final Object lock = new Object();


    @Before
    public void init() {
        pool = new FixedThreadPool(5);
        pool.start();
    }

    @Test
    @DisplayName("Проверка поведения программы при некорректном размере пула")
    public void checkIncorrectSizePool() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedThreadPool(0));

    }

    @Test
    @DisplayName("Проверка выполнения всех поставленных runnable задач")
    public void checkAmountOfExecutedRunnableTasks() {
        for (int i = 0; i < amount; i++) {
            pool.execute(() -> {
                synchronized (lock) {
                    count++;
                }
            });
        }
        while (count < 100) {
        }
        Assert.assertEquals(count, amount);
    }

    @Test
    @DisplayName("Проверка выполнения всех поставленных callable задач")
    public void checkAmountOfExecutedCallableTasks() throws ExecutionException, InterruptedException {
        for (int i = 0; i < amount; i++) {
            Future<Integer> future = pool.execute(() -> 1);

            synchronized (lock) {
                count += future.get();
            }
        }

        Assert.assertEquals(count, amount);
    }

    @Test
    @DisplayName("Тест поведения программы после завершения работы пула")
    public void testStoppedPool() {
        FixedThreadPool fixedThreadPool = new FixedThreadPool(1);
        fixedThreadPool.start();
        fixedThreadPool.stopNow();
        Assertions.assertThrows(NullPointerException.class, () -> fixedThreadPool.execute(() -> {
        }));
    }

    @After
    public void stop() {
        pool.stopNow();
    }

}
