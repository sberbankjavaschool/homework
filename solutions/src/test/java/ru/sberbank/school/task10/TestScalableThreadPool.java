package ru.sberbank.school.task10;

import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class TestScalableThreadPool extends TestsThreadPool {

    private ScalableThreadPool pool;


    @Before
    public void initialize() {
        pool = new ScalableThreadPool(3, 5);
        pool.start();
    }

    @Test
    public void checkRepeatStart() {
        super.checkRepeatStart(pool);
    }

    @Test
    public void checkIncrement() throws InterruptedException {
        super.checkIncrement(pool, 1000000, 1000);
    }

    @Test
    public void checkAdequacy() throws InterruptedException {
        super.checkAdequacy(pool, 100000, 10000);
    }

    @After
    public void stopPool() {
        pool.stopNow();
    }

}

