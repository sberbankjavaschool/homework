package ru.sberbank.school.task09;

import lombok.NonNull;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mart
 * 18.06.2019
 **/
public class RouteServiceImpl extends RouteService<City, Route<City>> {
    private File caheFile;
    private RouteSerializableManager serializableManager;

    public RouteServiceImpl (@NonNull String filepath, RouteSerializableManager serializableManager) {
        super(filepath);
        this.serializableManager = serializableManager;
    }

    @Override
    public Route<City> getRoute(@NonNull String from, @NonNull String to) {
        return null;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(@NonNull List<City> cities) {
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
