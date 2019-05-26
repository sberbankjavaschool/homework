package ru.sberbank.school.task09;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * ПРИМЕР
 */
public class InMemoryRouteService extends RouteService<City, Route<City>> {
    private HashMap<String, Route<City>> routeHashMap = new HashMap<>();

    public InMemoryRouteService() {

    }

    public Route<City> getRoute(String from, String to) {
        String key = from + "_" + to;
        Route<City> route = routeHashMap.get(key);
        if (route == null) {
            route = super.getRouteInner(from, to);
            routeHashMap.put(key, route);
        }
        return route;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(List<City> cities) {
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
