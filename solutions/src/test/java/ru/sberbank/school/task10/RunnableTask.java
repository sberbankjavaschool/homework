package ru.sberbank.school.task10;

import lombok.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class RunnableTask implements Runnable {

    private int a;
    private int b;
    private int millis;
    private int c;
    private boolean done;

    public RunnableTask(int a, int b, int millis) {
        this.a = a;
        this.b = b;

        if (millis == 0) {
            this.millis = 1000;
        } else {
            this.millis = millis;
        }

    }

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
        String s = Thread.currentThread().getName() + " a=" + a + " b=" + b;
        try {
            System.out.println(s + " start " + dateFormat.format(new Date()));
            Thread.sleep(millis);
            c = a + b;
            done = true;
            System.out.println(s + " finish " + " done=" + done + " c=" + c + " " + dateFormat.format(new Date()));
        } catch (InterruptedException e) {
            System.out.println(s + " was interrupted! " + dateFormat.format(new Date()));
        }
    }
}
