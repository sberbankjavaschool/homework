package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDate;
import java.util.LinkedList;

public class CityKryoSerializer extends Serializer<City> {

    @Override
    public void write(Kryo kryo, Output output, City object) {
        output.writeInt(object.getId());
        output.writeString(object.getCityName());
        output.writeLong(object.getNumberOfInhabitants());
        kryo.writeObject(output, object.getFoundDate());
        kryo.writeObject(output, object.getNearCities());
    }

    @SuppressWarnings("unchecked")
    @Override
    public City read(Kryo kryo, Input input, Class<? extends City> type) {
        City city = kryo.newInstance(type);
        kryo.reference(city);
        city.setId(input.readInt());
        city.setCityName(input.readString());
        city.setNumberOfInhabitants(input.readLong());
        city.setFoundDate(kryo.readObject(input, LocalDate.class));
        city.setNearCities(kryo.readObject(input, LinkedList.class));
        return city;
    }
}
