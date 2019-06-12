package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ConcurrentFixedThreadPoolTest {

    private RunnableTask r1;
    private RunnableTask r2;
    private RunnableTask r3;
    private CallableTask c1;
    private Future<Integer> f1;
    private ConcurrentFixedThreadPool fixedPool;
    private CountDownLatch latch;

    @BeforeEach
    void init() {
        latch = new CountDownLatch(4);
        r1 = new RunnableTask(3, 6, 1000, latch);
        r2 = new RunnableTask(1, 1, 1000, latch);
        r3 = new RunnableTask(5, 5, 1000, latch);
        c1 = new CallableTask(7, 8, 1000, latch);
    }

    private void initFixed(int amount) {
        fixedPool = new ConcurrentFixedThreadPool(amount);
        fixedPool.start();
    }

    private void exFixed() {
        fixedPool.execute(r1);
        fixedPool.execute(r2);
        fixedPool.execute(r3);
        f1 = fixedPool.execute(c1);
    }

    @Test
    @DisplayName("Проверка выброса исключения при подачи некорректных данных.")
    void fixedIllegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> initFixed(-4));
    }

    @Test
    @DisplayName("Проверка выброса исключения при вызове метода execute после остановки ThreadPool.")
    void fixedIllegalStateEx() {
        initFixed(4);
        exFixed();
        fixedPool.stopNow();

        Assertions.assertThrows(IllegalStateException.class, this::exFixed);
    }

    @Test
    @DisplayName("Проверка выброса исключения при вызове метода start после запуска ThreadPool.")
    void fixedIllegalStateStart() {
        initFixed(4);
        exFixed();
        Assertions.assertThrows(IllegalStateException.class, () -> fixedPool.start());
        fixedPool.stopNow();
    }

    @Test
    @DisplayName("Проверка выполненяи всех задач.")
    void fixedExTest() {
        initFixed(3);
        exFixed();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assertions.assertTrue(r1.isDone());
        Assertions.assertEquals(9, r1.getC());

        Assertions.assertTrue(r2.isDone());
        Assertions.assertEquals(2, r2.getC());

        Assertions.assertTrue(r3.isDone());
        Assertions.assertEquals(10, r3.getC());

        Assertions.assertTrue(f1.isDone());
        try {
            Assertions.assertEquals(15, f1.get().intValue());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        } catch (ExecutionException e) {
            System.out.println(e.toString());
        }
        fixedPool.stopNow();
    }

}
