package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Solution(9)
public class InMemoryRouteServiceSerializableManager extends RouteService<City, Route<City>> {

    private Kryo kryo;

    InMemoryRouteServiceSerializableManager(@NonNull String path) {
        super(path);
        initialize();
    }

    public void initialize() {
        kryo = new Kryo();
        kryo.setReferences(true);
        kryo.register(City.class, new SerializableCity());
        kryo.register(Route.class, new SerializableRoute());
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(LocalDate.class);
    }

    @Override
    public Route getRoute(@NonNull String from, @NonNull String to) {
        String fileName = from + to;
        Route route;
        File file = new File(path + File.separator + fileName);
        if (file.exists()) {
            route = deserializeRoute(file, from, to);
        } else {
            route = getRouteInner(from, to);
            serializeRoute(file, route);
        }
        return route;
    }

    public void serializeRoute(File file, Route<City> route) {
        try (OutputStream out = new FileOutputStream(file);
                Output output = new Output(out)) {
            kryo.writeObjectOrNull(output, route, Route.class);
        } catch (IOException ex) {
            throw new RouteFetchException(ex);
        }
    }

    public Route deserializeRoute(File file, String from, String to) {
        try (InputStream in = new FileInputStream(file);
                Input input = new Input(in)) {
            return (Route<City>) kryo.readObjectOrNull(input, Route.class);
        } catch (IOException ex) {
            throw new RouteFetchException(ex);
        }
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route createRoute(@NonNull List cities) {
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
