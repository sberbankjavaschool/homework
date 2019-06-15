package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task09.kryo.CitySerializer;
import ru.sberbank.school.task09.kryo.RouteSerializer;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RouteCacheService extends RouteService<City, Route<City>> {

    private Kryo kryo;
    private HashMap<String, Route<City>> routeHashMap = new HashMap<>();


    public void initialize() {
        this.kryo = new Kryo();
        kryo.setReferences(true);
        kryo.register(Route.class, new RouteSerializer());
        kryo.register(City.class, new CitySerializer());
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(LocalDate.class);
    }


    public void saveRoute(String key, Route route) {
        try (OutputStream outputStream = new FileOutputStream(key + ".txt");
             Output output = new Output(outputStream)) {
            kryo.writeObject(output, route);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public Route loadRoutes(String key) {
        Route routes = null;
        try (InputStream inputStream = new FileInputStream(key + ".txt");
             Input input = new Input(inputStream)) {
            routes = kryo.readObject(input, Route.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return routes;

    }

    @Override
    public Route<City> getRoute(String from, String to) {
        String key = from + "_" + to;
        Route<City> route = routeHashMap.get(key);

        if (route == null) {
            route = super.getRouteInner(from, to);
            saveRoute(key, route);
            routeHashMap.put(key, route);
            System.out.println("Serialized:");
            return route;
        } else {
            System.out.println("Deserialized:");
            route = loadRoutes(key);
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