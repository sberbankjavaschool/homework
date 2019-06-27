package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.LinkedList;
import java.util.List;


public class SerializableRoute extends Serializer<Route<City>> {


    @Override
    public void write(Kryo kryo, Output output, Route object) {
        output.writeString(object.getRouteName());
        kryo.writeObject(output, object.getCities());
    }

    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        Route<City> route = kryo.newInstance(type);
        route.setRouteName(input.readString());
        route.setCities((List<City>) kryo.readObject(input, List.class));
        return route;
    }
}