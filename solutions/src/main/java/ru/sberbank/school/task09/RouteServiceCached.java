package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import lombok.NonNull;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class RouteServiceCached extends RouteService<City, Route<City>> {
    private Kryo kryo;
    private String filesDirectory = "C:\\Users\\Anastasia\\Desktop\\Java\\task09Ser\\";
    private List<String> routes = new ArrayList<>();

    public RouteServiceCached() {
        kryo = new Kryo();
        RouteKryoSerializer kryoSerializer = new RouteKryoSerializer();
        kryo.register(Route.class, kryoSerializer);
        kryo.setReferences(true);
        kryo.register(City.class);
        kryo.register(LocalDate.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
    }

    @Override
    public Route<City> getRoute(@NonNull String from, @NonNull String to) {
        String key = from + "_" + to;
        Route route = null;
        if (routes.contains(key)) {
            route = loadRoute(key);
        } else {
            routes.add(key);
            route = saveRoute(key, from, to);
        }
        return route;
    }

    private Route<City> loadRoute(String key) {
        try (FileInputStream fis = new FileInputStream(filesDirectory + key + ".txt");
             Input in = new Input(fis)) {
            return (Route<City>) kryo.readObject(in, Route.class);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Route<City> saveRoute(String key, String from, String to) {
        Route route = super.getRouteInner(from, to);
        try (FileOutputStream fos = new FileOutputStream(filesDirectory + key + ".txt");
             Output out = new Output(fos)) {
            kryo.writeObject(out, route);
        } catch (NullPointerException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return route;
    }

    /**
     * Создает город.
     *
     * @param id                  Уникальный идентификатор города
     * @param cityName            Название
     * @param foundDate           Дата основания
     * @param numberOfInhabitants Число жителей
     * @return новую сущность города
     */
    @Override
    protected City createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        return new City(id, cityName, foundDate, numberOfInhabitants);
    }

    /**
     * Создает маршрут.
     *
     * @param cities Список всех сгенерированных городов
     * @return готовый маршрут
     */
    @Override
    protected Route<City> createRoute(@NonNull List<City> cities) {
        if (cities.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return new Route<>(UUID.randomUUID().toString(), cities);
    }
}
