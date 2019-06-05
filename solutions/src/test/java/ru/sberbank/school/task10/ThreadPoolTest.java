package ru.sberbank.school.task10;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadPoolTest {

    private RunnableTask r1;
    private RunnableTask r2;
    private RunnableTask r3;
    private CallableTask c1;
    private CallableTask c2;
    private FixedThreadPool fixPool;
    private ScalableThreadPool scalablePool;
    private Future<Integer> f1;
    private Future<Integer> f2;

    @BeforeEach
    void init() {
        r1 = new RunnableTask(3, 6, 1000);
        r2 = new RunnableTask(1, 1, 1000);
        r3 = new RunnableTask(5, 5, 1000);
        c1 = new CallableTask(7, 8, 1000);
        c2 = new CallableTask(9, 8, 1000);
    }

    private void initFixed(int n) {
        fixPool = new FixedThreadPool(n);
        fixPool.start();
    }

    private void exFixed() {
        fixPool.execute(r1);
        fixPool.execute(r2);
        fixPool.execute(r3);
        f1 = fixPool.execute(c1);
    }

    @Test
    @DisplayName("Проверка выполненяи всех задач при достаточном кол-ве времени.")
    void fixedExTest() {
        initFixed(4);
        exFixed();

        //задача еще Callable не успевает выполнится
        try {
            f1.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        } catch (ExecutionException e) {
            System.out.println(e.toString());
        } catch (TimeoutException e) {
            Assertions.assertFalse(f1.isDone());
        }

        //делаем таймаут, достаточного для выполнения задач
        sleep(1100);

        fixPool.stopNow();

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
    }

    private void initScalable(int min, int max) {
        scalablePool = new ScalableThreadPool(min, max);
        scalablePool.start();
    }

    private void exScalable() {
        scalablePool.execute(r1);
        f1 = scalablePool.execute(c1);
        scalablePool.execute(r2);
        scalablePool.execute(r3);
        f2 = scalablePool.execute(c2);
    }

    @Test
    @DisplayName("Проверка выполненяи всех задач при достаточном кол-ве времени.")
    void scalableExTest() {
        initScalable(2, 4);
        exScalable(); //5 задач

        //делаем таймаут, достаточного для выполнения задач
        sleep(2200);

        scalablePool.stopNow();

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

        Assertions.assertTrue(f2.isDone());
        try {
            Assertions.assertEquals(17, f2.get().intValue());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        } catch (ExecutionException e) {
            System.out.println(e.toString());
        }
    }

    @Test
    @DisplayName("Проверка запуска и остановки тередов.")
    void scalableStartStop() {
        initScalable(5, 6);
        exScalable();

        //Кол-во тредов после start
        Assertions.assertEquals(5, scalablePool.getThreadSize());
        scalablePool.stopNow();
        //Кол-во тредов после stop
        Assertions.assertEquals(0, scalablePool.getThreadSize());
    }

    @Test
    @DisplayName("Проверка добавления и удаления тредов.")
    void scalableAddDelThread() {
        initScalable(2, 3);

        scalablePool.execute(r1);
        sleep(100);

        //Создали min кол-во тредов и добавили 1 задачу
        Assertions.assertEquals(2, scalablePool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(1, scalablePool.getN());

        scalablePool.execute(r2);
        sleep(100);
        //Добавили задачу, треды не создаем, т.к. есть свободные
        Assertions.assertEquals(2, scalablePool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(2, scalablePool.getN());

        scalablePool.execute(r3);
        sleep(100);
        //Добавили задачи, задач в работе больше, чем кол-во тредов и кол-во тредов < max -> создаем тред
        Assertions.assertEquals(3, scalablePool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(3, scalablePool.getN());

        f1 = scalablePool.execute(c1);
        sleep(100);
        //Добавили задачи, задач в работе больше, чем кол-во тредов, кол-во тредов >= max -> не создаем тред
        Assertions.assertEquals(3, scalablePool.getThreadSize());
        //Кол-во тредов в работе
        Assertions.assertEquals(3, scalablePool.getN());

        sleep(1200);

        //Кол-во тредов после выполнения всех задач=min
        Assertions.assertEquals(2, scalablePool.getThreadSize());
        scalablePool.stopNow();
    }

    private void sleep(int i) {
        try {
            TimeUnit.MILLISECONDS.sleep(i);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }

}
