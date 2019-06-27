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
        kryo.writeObjectOrNull(output, object.getFoundDate(), LocalDate.class);
        output.writeLong(object.getNumberOfInhabitants());
        kryo.writeObjectOrNull(output, object.getNearCities(), List.class);
    }

    @Override
    public City read(Kryo kryo, Input input, Class<? extends City> type) {
        City city = kryo.newInstance(type);
        kryo.reference(city);
        city.setId(input.readInt());
        city.setCityName(input.readString());
        city.setFoundDate(kryo.readObjectOrNull(input, LocalDate.class));
        city.setNumberOfInhabitants(input.readLong());
        city.setNearCities(kryo.readObjectOrNull(input, List.class));
        return city;
    }
}
