package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;

import java.time.LocalDate;
import java.util.List;

@Solution(9)
public class RouteServiceReal extends RouteService<City, Route<City>> {

    private Kryo kryo;

    public RouteServiceReal(@NonNull String path) {
        super(path);
        this.kryo = new Kryo();
        kryo.register(Route.class, new RouteKryoSerializer());
        kryo.register(City.class, new CityKryoSerializer());
    }


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
