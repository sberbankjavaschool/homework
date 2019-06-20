package ru.sberbank.school.task09.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task09.City;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CitySerializer extends Serializer<City> {


//    private int id;
//    private String cityName;
//    private LocalDate foundDate;
//    private long numberOfInhabitants;
//    private List<City> nearCities;


    @Override
    public void write(Kryo kryo, Output output, City city) {

        output.writeInt(city.getId());
        output.writeString(city.getCityName());
        kryo.writeObjectOrNull(output, city.getFoundDate(), LocalDate.class);
        output.writeLong(city.getNumberOfInhabitants());
        kryo.writeObjectOrNull(output, city.getNearCities(), ArrayList.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public City read(Kryo kryo, Input input, Class<? extends City> type) {

        City city = kryo.newInstance(type);
//        kryo.setReferences(true);
        kryo.reference(city);
        city.setId(input.readInt());
        city.setCityName(input.readString());
        city.setFoundDate((LocalDate) kryo.readClassAndObject(input));
        city.setNumberOfInhabitants(input.readLong());
        city.setNearCities((List<City>) kryo.readClassAndObject(input));

        return city;
    }
}
