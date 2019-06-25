package ru.sberbank.school.task09;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Mart
 * 18.06.2019
 **/
public class RouteServiceImpl extends RouteService<City, Route<City>> {

    @Override
    public Route<City> getRoute(String from, String to) {
        return null;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return null;
    }

    @Override
    protected Route<City> createRoute(List<City> cities) {
        return null;
    }
}
