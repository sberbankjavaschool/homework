package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.LinkedList;

public class RouteKryoSerializer extends Serializer<Route<City>> {
    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
        kryo.writeObject(output, object.getCities());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route<City> route = kryo.newInstance(type);
        route.setRouteName(input.readString());
        route.setCities(kryo.readObject(input, LinkedList.class));
        return route;
    }
}
