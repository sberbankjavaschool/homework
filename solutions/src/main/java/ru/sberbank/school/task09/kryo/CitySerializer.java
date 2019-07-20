package ru.sberbank.school.task09.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task09.City;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by Mart
 * 02.07.2019
 **/
public class CitySerializer extends Serializer<City> {
    @Override
    public void write(Kryo kryo, Output output, City object) {
        output.writeInt(object.getId());
        output.writeString(object.getCityName());
        kryo.writeObjectOrNull(output, object.getFoundDate(), LocalDate.class);
        output.writeLong(object.getNumberOfInhabitants());
        kryo.writeObjectOrNull(output, object.getNearCities(), ArrayList.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public City read(Kryo kryo, Input input, Class<? extends City> type) {
        City city = kryo.newInstance(type);
        kryo.reference(city);

        city.setId(input.readInt());
        city.setCityName(input.readString());
        city.setFoundDate(kryo.readObjectOrNull(input, LocalDate.class));
        city.setNumberOfInhabitants(input.readLong());
        city.setNearCities(kryo.readObjectOrNull(input, ArrayList.class));

        return city;
    }
}
