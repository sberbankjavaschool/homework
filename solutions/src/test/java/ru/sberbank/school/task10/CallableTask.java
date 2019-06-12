package ru.sberbank.school.task10;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class CallableTask implements Callable<Integer> {

    private int a;
    private int b;
    private int millis;
    private CountDownLatch latch;

    public CallableTask(int a, int b, int millis) {
        this.a = a;
        this.b = b;
        if (millis == 0) {
            this.millis = 1000;
        } else {
            this.millis = millis;
        }
    }

    public CallableTask(int a, int b, int millis, CountDownLatch latch) {
        this(a, b, millis);
        this.latch = latch;
    }

    @Override
    public Integer call() throws Exception {
        if (a < 0) {
            throw new Exception("Parameter a < 0");
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
        String s = Thread.currentThread().getName() + " callable a=" + a + " b=" + b;
        try {
            System.out.println(s + " start " + dateFormat.format(new Date()));
            Thread.sleep(millis);
            Integer c = Integer.sum(a, b);
            System.out.println(s + " finish " + " c=" + c + " " + dateFormat.format(new Date()));
            if (latch != null) {
                latch.countDown();
            }
            return c;
        } catch (InterruptedException e) {
            System.out.println(s + " was interrupted! " + dateFormat.format(new Date()));
        }
        return null;
    }
}
