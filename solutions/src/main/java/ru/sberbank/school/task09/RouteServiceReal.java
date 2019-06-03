package ru.sberbank.school.task09;

import java.time.LocalDate;
import java.util.List;

public class RouteServiceReal extends RouteService {
    @Override
    public Route getRoute(String from, String to) {
        return null;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return null;
    }

    @Override
    protected Route createRoute(List cities) {
        return null;
    }
}
