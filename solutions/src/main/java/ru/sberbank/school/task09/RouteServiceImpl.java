package ru.sberbank.school.task09;

import ru.sberbank.school.util.Solution;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Solution(9)
public class RouteServiceImpl extends RouteService<City, Route<City>> {

    private CacheService cacheService;

    public RouteServiceImpl(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Route<City> getRoute(String from, String to) {
        Optional<Route<City>> optionalCityRoute = cacheService.load(from + "_" + to);
        if (optionalCityRoute.isPresent()) {
            return optionalCityRoute.get();
        } else {
            Route<City> route = getRouteInner(from, to);
            cacheService.save(from + "_" + to, route);
            return route;
        }
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
