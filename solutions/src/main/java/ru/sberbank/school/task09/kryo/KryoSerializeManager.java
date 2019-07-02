package ru.sberbank.school.task09.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;
import ru.sberbank.school.task09.RouteSerializeManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Mart
 * 01.07.2019
 **/
public class KryoSerializeManager implements RouteSerializeManager<Route<City>, City> {
    private Kryo kryo;
    private String directoryPath;

    public KryoSerializeManager(@NonNull String directoryPath) {
        this.directoryPath = directoryPath;
        this.kryo = new Kryo();
        kryo.register(Route.class, new RouteSerializer());
        kryo.register(City.class, new CitySerializer());
        kryo.register(LocalDate.class);
        kryo.register(ArrayList.class);
        kryo.register(LinkedList.class);
        kryo.setReferences(true);

    }

    @Override
    public void saveRoute(@NonNull String fileName,@NonNull Route<City> route) {
        try (Output output = new Output(new FileOutputStream(directoryPath + File.separator + fileName))) {
            kryo.writeClassAndObject(output, route);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Route<City> loadRoute(@NonNull String fileName) {
        Route<City> route = null;
        try (Input input = new Input(new FileInputStream(directoryPath + File.separator + fileName))) {
            route = (Route<City>) kryo.readClassAndObject(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return route;
    }

}
