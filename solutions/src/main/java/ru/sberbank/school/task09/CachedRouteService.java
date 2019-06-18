package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static java.io.File.separator;

@Solution(9)
public class CachedRouteService extends RouteService<City, Route<City>> {

    private final Set<String> routeKeys = new HashSet<>();

    private final Kryo kryo;

    public CachedRouteService(@NonNull String path) {
        super(path);
        kryo = new Kryo();
        initialize();
    }

    public void initialize() {
        kryo.setReferences(true);
        kryo.register(String.class);
        kryo.register(LocalDate.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.register(City.class, new CitySerializer());
        kryo.register(Route.class, new RouteSerializer());
    }

    @Override
    public Route<City> getRoute(String from, String to) {
        String key = from + "_" + to;
        File file = new File(path + separator + key);
        Route<City> route;
        if (routeKeys.contains(key)) {
            route = fetchRoute(file);
        } else {
            route = getRouteInner(from, to);
            cacheRoute(file, route);
            routeKeys.add(key);
        }
        return route;
    }

    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    @Override
    protected Route<City> createRoute(List<City> cities) {
        if (cities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }

    private void cacheRoute(@NonNull File file, @NonNull Route<City> route) throws RouteFetchException {
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeClassAndObject(output, route);
        } catch (IOException | NullPointerException | KryoException e) {
            throw new RouteFetchException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Route<City> fetchRoute(@NonNull File file) throws RouteFetchException {
        try (Input input = new Input(new FileInputStream(file))) {
            return (Route<City>) kryo.readClassAndObject(input);
        } catch (IOException | NullPointerException | ClassCastException | KryoException e) {
            throw new RouteFetchException(e);
        }
    }

    public static class CitySerializer extends Serializer<City> {

        @Override
        public void write(Kryo kryo, Output output, City object) {
            output.writeInt(object.getId());
            output.writeString(object.getCityName());
            output.writeLong(object.getNumberOfInhabitants());
            kryo.writeObjectOrNull(output, object.getFoundDate(), LocalDate.class);
            kryo.writeObjectOrNull(output, object.getNearCities(), ArrayList.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public City read(Kryo kryo, Input input, Class<? extends City> type) {
            City city = kryo.newInstance(type);
            kryo.reference(city);
            city.setId(input.readInt());
            city.setCityName(input.readString());
            city.setNumberOfInhabitants(input.readLong());
            city.setFoundDate(kryo.readObjectOrNull(input, LocalDate.class));
            List<City> cities = kryo.readObjectOrNull(input, ArrayList.class);
            if (Objects.isNull(cities)) {
                cities = new ArrayList<>();
            }
            city.setNearCities(cities);
            return city;
        }
    }

    public static class RouteSerializer extends Serializer<Route<City>> {
        @Override
        public void write(Kryo kryo, Output output, Route<City> object) {
            output.writeString(object.getRouteName());
            kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
            Route route = kryo.newInstance(type);
            route.setRouteName(input.readString());

            List<City> cities = kryo.readObjectOrNull(input, LinkedList.class);
            if (Objects.isNull(cities)) {
                cities = new LinkedList<>();
            }
            route.setCities(cities);
            return route;
        }
    }
}