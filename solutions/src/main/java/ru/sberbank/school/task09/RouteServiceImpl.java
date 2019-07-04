package ru.sberbank.school.task09;

import lombok.NonNull;
import ru.sberbank.school.task09.kryo.KryoSerializeManager;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mart
 * 18.06.2019
 **/
@Solution(9)
public class RouteServiceImpl extends RouteService<City, Route<City>> {
    private RouteSerializeManager serializeManager;

    public RouteServiceImpl(@NonNull String filepath, RouteSerializeManager serializeManager) {
        super(filepath);
        if (filepath.isEmpty()) {
            throw new IllegalArgumentException("directory path couldn't be empty line");
        }

        this.serializeManager = serializeManager;
    }

    /*
     *constructor with default serializer
     */
    public RouteServiceImpl(@NonNull String path) {
        super(path);
        if (path.isEmpty()) {
            throw new IllegalArgumentException("directory path couldn't be empty line");
        }
        serializeManager = new KryoSerializeManager(path);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Route<City> getRoute(@NonNull String from, @NonNull String to) {
        if (from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("City Names couldn't be empty line");
        }
        String routeName = from + "_" + to;
        Route<City> route = getFromCache(routeName);

        if (route == null) {
            route = getRouteInner(from, to);
            serializeManager.saveRoute(routeName, route);
        }
        return route;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(@NonNull List<City> cities) {
        if (cities.isEmpty()) {
            throw new IllegalArgumentException("List of cities couldn't be empty list");
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }

    @SuppressWarnings("unchecked")
    private Route<City> getFromCache(String routeName) {
        Route<City> route = null;
        File file = new File(path + File.separator + routeName);

        if (file.exists()) {
            route = serializeManager.loadRoute(routeName);
        }
        return route;
    }
}
