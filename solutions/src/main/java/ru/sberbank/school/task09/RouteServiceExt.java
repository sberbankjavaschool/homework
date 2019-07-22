package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import lombok.NonNull;
import org.objenesis.strategy.StdInstantiatorStrategy;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Solution(9)
public class RouteServiceExt extends RouteService<City, Route<City>> {

    private Kryo kryo;

    public RouteServiceExt(@NonNull String path) {
        super(path);
        this.kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(Route.class, new RouteKryoSerializer());
        kryo.register(City.class, new CityKryoSerializer());
        kryo.register(String.class);
        kryo.register(LocalDate.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
    }

    @Override
    public Route<City> getRoute(@NonNull String from,@NonNull String to) {
        if (from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("Имя города не может быть пустое");
        }
        Route<City> route;
        File file = getFile(from, to);
        if (file.exists()) {
            route = readRoute(file);
        } else {
            route = getRouteInner(from, to);
            writeRoute(file, route);
        }
        return route;
    }

    private File getFile(String from, String to) {
        String fileName = from + "_" + to;
        return new File(path + File.separator + fileName);
    }

    private Route<City> readRoute(File file) {
        try (FileInputStream fis = new FileInputStream(file);
                Input input = new Input(fis)) {
            return (Route<City>) kryo.readObjectOrNull(input, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeRoute(File file, Route<City> route) {
        try (FileOutputStream fos = new FileOutputStream(file);
                Output output = new Output(fos)) {
            kryo.writeObjectOrNull(output, route, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(@NonNull List cities) {
        if (cities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
