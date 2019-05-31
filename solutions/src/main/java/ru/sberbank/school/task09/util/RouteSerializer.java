package ru.sberbank.school.task09.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task09.City;
import ru.sberbank.school.task09.Route;

import java.util.LinkedList;
import java.util.List;

public class RouteSerializer extends Serializer<Route<City>> {

    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
    }

    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        String name = input.readString();
        LinkedList<City> cities = kryo.readObjectOrNull(input, LinkedList.class);

        Route<City> route = kryo.newInstance(type);
        kryo.reference(route);
        route.setRouteName(name);
        route.setCities(cities);
        return route;
    }

}