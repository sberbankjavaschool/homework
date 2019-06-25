package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDate;
import java.util.List;

public class SerializableCity extends Serializer<City> {
    @Override
    public void write(Kryo kryo, Output output, City object) {
        output.writeInt(object.getId());
        output.writeString(object.getCityName());
        kryo.writeObject(output, object.getFoundDate());
        output.writeLong(object.getNumberOfInhabitants());
        kryo.writeObject(output, object.getNearCities());
    }

    @Override
    public City read(Kryo kryo, Input input, Class<? extends City> type) {
        City city = kryo.newInstance(type);
        city.setId(input.readInt());
        city.setCityName(input.readString());
        city.setFoundDate(kryo.readObject(input, LocalDate.class));
        city.setNumberOfInhabitants(input.readLong());
        city.setNearCities(kryo.readObject(input, List.class));
        return city;
    }
}
