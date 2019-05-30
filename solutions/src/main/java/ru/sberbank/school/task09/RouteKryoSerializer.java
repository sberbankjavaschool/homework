package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RouteKryoSerializer extends Serializer<Route<City>> {

    @Override
    public void write(Kryo kryo, Output output, Route<City> object) {
        output.writeString(object.getRouteName());
        kryo.writeObjectOrNull(output, object.getCities(), LinkedList.class);
//        for (City c : object.getCities()) {
//            output.writeInt(c.getId());
//            output.writeString(c.getCityName());
//            output.writeString(c.getFoundDate().toString());
//            output.writeLong(c.getNumberOfInhabitants());
//            for (City nc : c.getNearCities()) {
//                output.writeInt(nc.getId());
//                output.writeString(nc.getCityName());
//                output.writeString(nc.getFoundDate().toString());
//                output.writeLong(nc.getNumberOfInhabitants());
//            }
//            //kryo.writeObjectOrNull(output, c.getNearCities(), ArrayList.class);
//        }

    }

    @Override
    public Route<City> read(Kryo kryo, Input input, Class<? extends Route<City>> type) {
        String name = input.readString();
        List<City> cityObjects = kryo.readObjectOrNull(input, LinkedList.class);
        return new Route<>(name, cityObjects);
    }
}
