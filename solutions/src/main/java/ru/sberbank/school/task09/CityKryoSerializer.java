package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.MapState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CityKryoSerializer extends Serializer<City> {


    @Override
    public void write(Kryo kryo, Output output, @NonNull City object) {
        output.writeInt(object.getId());
        output.writeString(object.getCityName());
        output.writeLong(object.getNumberOfInhabitants());
        kryo.writeObjectOrNull(output, object.getFoundDate(), LocalDate.class);
        kryo.writeObjectOrNull(output, object.getNearCities(), ArrayList.class);
    }

    @Override
    public City read(Kryo kryo, Input input, Class<? extends City> type) {
        City city = kryo.newInstance(type);
        kryo.reference(city);
        city.setId(input.readInt());
        city.setCityName(input.readString());
        city.setNumberOfInhabitants(input.readLong());
        city.setFoundDate(kryo.readObjectOrNull(input, LocalDate.class));
        city.setNearCities(kryo.readObjectOrNull(input, ArrayList.class));
        return city;
    }
}
