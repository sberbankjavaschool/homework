package ru.sberbank.school.task10;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class CallableTask implements Callable<Integer> {

    private int a;
    private int b;
    private int millis;

    public CallableTask(int a, int b, int millis) {
        this.a = a;
        this.b = b;
        if (millis == 0) {
            this.millis = 1000;
        } else {
            this.millis = millis;
        }
    }


    @Override
    public Integer call() throws Exception {
        if (a < 0) {
            throw new IllegalArgumentException("Parameter a < 0");
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
        String s = Thread.currentThread().getName() + " callable a=" + a + " b=" + b;
        try {
            System.out.println(s + " start " + dateFormat.format(new Date()));
            Thread.sleep(millis);
            Integer c = Integer.sum(a, b);
            System.out.println(s + " finish " + " c=" + c + " " + dateFormat.format(new Date()));
            return c;
        } catch (InterruptedException e) {
            System.out.println(s + " was interrupted! " + dateFormat.format(new Date()));
        }
        return null;
    }
}
