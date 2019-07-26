package ru.sberbank.school.task13.cache;

import java.io.Serializable;

/**
 * Created by Mart
 * 20.07.2019
 **/
public class Temp implements Serializable {
    private String superTemp;

    public Temp(Double superTemp) {
        this.superTemp = superTemp.toString();
    }

    public String getSuperTemp() {
        return superTemp;
    }

    public void setSuperTemp(Double superTemp) {
        this.superTemp = superTemp.toString();
    }

    @Override
    public String toString() {
        return "Temp{" +
                "superTemp='" + superTemp + '\'' +
                '}';
    }
}
