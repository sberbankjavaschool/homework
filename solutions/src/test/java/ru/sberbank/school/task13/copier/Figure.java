package ru.sberbank.school.task13.copier;

public class Figure {

    private float x;

    Figure(float x) {
        this.x = x;
    }

    public float getX(int i) {
        return x;
    }

    public float setX(float x) {
        this.x = x;
        return 1;
    }

}
