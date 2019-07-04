package ru.sberbank.school.task09.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;

import java.util.LinkedList;

/**
 * Created by Mart
 * 02.07.2019
 **/
public class RouteSerializer extends Serializer<Route<City>> {
    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route<City> route = kryo.newInstance(type);
        kryo.reference(route);

        route.setRouteName(input.readString());
        route.setCities(kryo.readObjectOrNull(input, LinkedList.class));
        return route;
    }
}
