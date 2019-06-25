package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class InMemoryRouteServiceSerializableManager extends RouteService{

    private Kryo kryo;

    @Override
    public Route getRoute(String from, String to) {
        return null;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route createRoute(List cities) {
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
