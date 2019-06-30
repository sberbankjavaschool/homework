package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task09.kryo.CitySerializer;
import ru.sberbank.school.task09.kryo.RouteSerializer;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Solution(9)
public class RouteCacheService extends RouteService<City, Route<City>> {

    private Kryo kryo;

    public RouteCacheService(@NonNull String path) {
        super(path);
        initialize();
    }


    public void initialize() {
        this.kryo = new Kryo();
        kryo.setReferences(true);
        kryo.register(Route.class, new RouteSerializer());
        kryo.register(City.class, new CitySerializer());
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(LocalDate.class);
    }


    public void saveRoute(File file, Route<City> route) {
        try (OutputStream outputStream = new FileOutputStream(file);
             Output output = new Output(outputStream)) {
            kryo.writeObjectOrNull(output, route, Route.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public Route<City> loadRoutes(File file) {
        Route routes = null;
        try (InputStream inputStream = new FileInputStream(file);
             Input input = new Input(inputStream)) {
            routes = kryo.readObjectOrNull(input, Route.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return routes;

    }

    @Override
    public Route<City> getRoute(String from, String to) {
        String key = from + "_" + to;

        File file = new File(path + File.separator + key);
        System.out.println(file.getPath());
        Route<City> route;

        if (!file.exists()) {
            route = getRouteInner(from, to);

            saveRoute(file, route);

            System.out.println("Serialized: " + key);
            System.out.println("Route: " + route.toString());

            return route;
        } else {

            System.out.println("Deserialized: " + key);

            route = loadRoutes(file);

            System.out.println("Route: " + route.toString());

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
