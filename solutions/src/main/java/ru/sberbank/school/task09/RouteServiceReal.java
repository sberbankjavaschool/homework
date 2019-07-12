package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
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
public class RouteServiceReal extends RouteService<City, Route<City>> {

    private Kryo kryo;

    public RouteServiceReal(@NonNull String path) {
        super(path);
        this.kryo = new Kryo();
        kryo.register(Route.class, new RouteKryoSerializer());
        kryo.register(City.class, new CityKryoSerializer());
        kryo.setReferences(true);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.register(String.class);
        kryo.register(LocalDate.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
    }

    @Override
    public Route getRoute(@NonNull String from,@NonNull String to) {//TODO
        String key = from + "_" + to;
        return null;
    }

    private Route<City> readRoute(String key) {
        String file = path + File.separator + key + ".txt";
        try (FileInputStream fis = new FileInputStream(file);
             Input input = new Input(fis)) {
            return (Route<City>) kryo.readObjectOrNull(input, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeRoute(String key, Route<City> route) {
        String file = path + File.separator + key + ".txt";
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
    protected Route createRoute(@NonNull List cities) {
        if (cities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
