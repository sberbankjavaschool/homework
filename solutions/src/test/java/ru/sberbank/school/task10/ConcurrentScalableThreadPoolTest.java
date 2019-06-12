package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ConcurrentScalableThreadPoolTest {

    private RunnableTask r1;
    private RunnableTask r2;
    private RunnableTask r3;
    private CallableTask c1;
    private CallableTask c2;
    private Future<Integer> f1;
    private Future<Integer> f2;
    private ConcurrentScalableThreadPool threadPool;
    private CountDownLatch latch;

    void init(int n) {
        latch = new CountDownLatch(n);
        r1 = new RunnableTask(3, 6, 1000, latch);
        r2 = new RunnableTask(1, 1, 1000, latch);
        r3 = new RunnableTask(5, 5, 1000, latch);
        c1 = new CallableTask(7, 8, 1000, latch);
        c2 = new CallableTask(9, 8, 1000, latch);
    }

    private void initScalable(int min, int max) {
        threadPool = new ConcurrentScalableThreadPool(min, max);
        threadPool.start();
    }

    private void exScalable() {
        threadPool.execute(r1);
        f1 = threadPool.execute(c1);
        threadPool.execute(r2);
        threadPool.execute(r3);
        f2 = threadPool.execute(c2);
    }

    @Test
    @DisplayName("Проверка выброса исключения при подачи некорректных данных.")
    void scalableIllegalArgument() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> initScalable(-4, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> initScalable(1, -1));
    }

    @Test
    @DisplayName("Проверка выброса исключения при вызове метода execute после остановки ThreadPool.")
    void scalableIllegalStateEx() {
        init(5);
        initScalable(4, 6);
        exScalable();
        threadPool.stopNow();

        Assertions.assertThrows(IllegalStateException.class, this::exScalable);
    }

    @Test
    @DisplayName("Проверка выброса исключения при вызове метода start после запуска ThreadPool.")
    void scalableIllegalStateStart() {
        init(5);
        initScalable(4, 6);
        exScalable();
        Assertions.assertThrows(IllegalStateException.class, () -> threadPool.start());
        threadPool.stopNow();
    }

    @Test
    @DisplayName("Проверка выполненяи всех задач.")
    void scalableExTest() {
        init(5);
        initScalable(2, 4);
        exScalable();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.stopNow();

        Assertions.assertTrue(r1.isDone());
        Assertions.assertEquals(9, r1.getC());

        Assertions.assertTrue(r2.isDone());
        Assertions.assertEquals(2, r2.getC());

        Assertions.assertTrue(r3.isDone());
        Assertions.assertEquals(10, r3.getC());

        Assertions.assertTrue(f1.isDone());
        try {
            Assertions.assertEquals(15, f1.get().intValue());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.toString());
        }

        Assertions.assertTrue(f2.isDone());
        try {
            Assertions.assertEquals(17, f2.get().intValue());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    @DisplayName("Проверка запуска и остановки тредов.")
    void scalableStartStop() {
        init(5);
        initScalable(2, 6);

        //Кол-во тредов после start
        Assertions.assertEquals(2, threadPool.getThreadSize());
        exScalable();
        threadPool.stopNow();
        //Кол-во тредов после stop
        Assertions.assertEquals(0, threadPool.getThreadSize());
    }

    @Test
    @DisplayName("Проверка добавления и удаления тредов при разной загруженности пула.")
    void scalableAddDelThread() {
        init(4);
        initScalable(2, 3);

        threadPool.execute(r1);
        sleep(100);

        //Создали min кол-во тредов и добавили 1 задачу
        Assertions.assertEquals(2, threadPool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(1, threadPool.getCountWorker());

        threadPool.execute(r2);
        sleep(100);
        //Добавили задачу, треды не создаем, т.к. есть свободные
        Assertions.assertEquals(2, threadPool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(2, threadPool.getCountWorker());

        threadPool.execute(r3);
        sleep(100);
        //Добавили задачу, задач в работе больше, чем кол-во тредов и кол-во тредов < max -> создаем тред
        Assertions.assertEquals(3, threadPool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(3, threadPool.getCountWorker());

        f1 = threadPool.execute(c1);
        sleep(100);
        //Добавили задачи, задач в работе больше, чем кол-во тредов, кол-во тредов >= max -> не создаем тред
        Assertions.assertEquals(3, threadPool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(3, threadPool.getCountWorker());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Кол-во тредов после выполнения всех задач = min
        Assertions.assertEquals(2, threadPool.getThreadSize());
        threadPool.stopNow();
    }

    private void sleep(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
}
