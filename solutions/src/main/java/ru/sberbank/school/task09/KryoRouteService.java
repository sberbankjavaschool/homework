package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.ReferenceResolver;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.TimeSerializers;
import com.esotericsoftware.kryo.util.ListReferenceResolver;
import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Solution(9)
public class KryoRouteService extends RouteService<City, Route<City>> {

    private Kryo kryo;

    public KryoRouteService(@NonNull String path) {
        super(path);
        initialize();
    }

    private void initialize() {
        kryo = new Kryo();

        kryo.setDefaultSerializer(TaggedFieldSerializer.class);
        kryo.setReferences(true);
        kryo.setReferenceResolver(new ListReferenceResolver());

        kryo.register(Route.class, new RouteSerializer());
        kryo.register(City.class, new CitySerializer());
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(LocalDate.class);
        kryo.register(String.class);

        TimeSerializers.addDefaultSerializers(kryo);
    }

    @Override
    public Route<City> getRoute(String from, String to) {
        String key = from + "_" + to;
        Route<City> route;

        File file = new File(path + File.separator + key);

        if (file.exists()) {
            route = readRoute(file);
        } else {
            route = getRouteInner(from, to);
            cacheRoute(file, route);
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

    public void cacheRoute(@NonNull File file, @NonNull Route<City> route) throws RouteFetchException {
        try (OutputStream os = new FileOutputStream(file);
                              Output out = new Output(os)) {

            kryo.writeObjectOrNull(out, route, Route.class);

        } catch (IOException  e) {
            throw new RouteFetchException(e);
        }
    }

    public Route<City> readRoute(@NonNull File file) throws RouteFetchException {
        Route<City> route;

        try (InputStream is = new FileInputStream(file);
                            Input input = new Input(is)) {

            route = kryo.readObjectOrNull(input, Route.class);

        } catch (IOException e) {
            throw new RouteFetchException(e);
        }

        return route;
    }
}
