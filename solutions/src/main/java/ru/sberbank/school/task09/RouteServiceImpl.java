package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import org.objenesis.strategy.StdInstantiatorStrategy;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;

@Solution(9)
public class RouteServiceImpl extends RouteService<City, Route<City>> {
    private Kryo kryo;

    public RouteServiceImpl(@NonNull String path) {
        super(path);
        kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(Route.class, new RouteSerializer());
        kryo.register(String.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(City.class, new CitySerializer());
        kryo.register(LocalDate.class);
    }

    public Route<City> getRoute(@NonNull String from, @NonNull String to) {
        String key = from + "_" + to;
        Route<City> route;
        File file = new File(path + File.separator + key);
        if (file.exists()) {
            route = readRouteFromFile(file, from, to);
        } else {
            route = getRouteInner(from, to);
            writeRouteInFile(file, route);
        }
        return route;
    }

    private void writeRouteInFile(File file, Route<City> route) {
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObjectOrNull(output, route, Route.class);
        } catch (KryoException | FileNotFoundException e) {
            throw new RouteFetchException(e);
        }
    }

    private Route<City> readRouteFromFile(File file, String from, String to) {
        try (Input input = new Input(new FileInputStream(file))) {
            return (Route<City>) kryo.readObjectOrNull(input, Route.class);
        } catch (KryoException | FileNotFoundException e) {
            return getRouteInner(from, to);
        }
    }

    @Override
    protected City createCity(int id,
                              String cityName,
                              LocalDate foundDate,
                              long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(@NonNull List<City> cities) {
        if (cities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
