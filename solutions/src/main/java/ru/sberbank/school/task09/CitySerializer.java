package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalDate;
import java.util.ArrayList;

public class CitySerializer extends Serializer<City> {
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
        City result = kryo.newInstance(type);
        kryo.reference(result);

        result.setId(input.readInt());
        result.setCityName(input.readString());
        result.setFoundDate(kryo.readObject(input, LocalDate.class));
        result.setNumberOfInhabitants(input.readLong());
        result.setNearCities(kryo.readObject(input, ArrayList.class));

        return result;
    }
}
