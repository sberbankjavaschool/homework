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
import java.util.*;

@Solution(9)
public class RouteServiceCached extends RouteService<City, Route<City>> {
    private Kryo kryo;
    private List<String> routes = new ArrayList<>();

    public RouteServiceCached(@NonNull String path) {
        super(path);
        kryo = new Kryo();
        RouteKryoSerializer kryoSerializer = new RouteKryoSerializer();
        kryo.register(Route.class, kryoSerializer);
        kryo.setReferences(true);
        kryo.register(City.class);
        kryo.register(LocalDate.class);
        kryo.register(LinkedList.class);
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

//    private void initialization() {
//
//    }

    @Override
    public Route<City> getRoute(@NonNull String from, @NonNull String to) {
        String key = from + "_" + to;
        Route route;
        if (routes.contains(key)) {
            route = loadRoute(key);
        } else {
            route = super.getRouteInner(from, to);
            saveRoute(key, route);
            routes.add(key);
        }
        return route;
    }

    private Route<City> loadRoute(String key) {
        String file = path + File.separator + key + ".txt";
        try (FileInputStream fis = new FileInputStream(file);
             Input in = new Input(fis)) {
            return (Route<City>) kryo.readObject(in, Route.class);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void saveRoute(String key, Route route) {
        String file = path + File.separator + key + ".txt";
        //Route route = super.getRouteInner(from, to);
        try (FileOutputStream fos = new FileOutputStream(file);
             Output out = new Output(fos)) {
            kryo.writeObject(out, route);
        } catch (NullPointerException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
