package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CityKryoSerializer extends Serializer<City> {

    @Override
    public void write(Kryo kryo, @NonNull Output output, @NonNull City object) {
        output.writeInt(object.getId());
        output.writeString(object.getCityName());
        kryo.writeObjectOrNull(output, object.getFoundDate(), LocalDate.class);
        output.writeLong(object.getNumberOfInhabitants());
        kryo.writeObjectOrNull(output, object.getNearCities(), ArrayList.class);
    }

    @Override
    public City read(Kryo kryo, @NonNull Input input, Class<? extends City> type) {
        Integer id = input.readInt();
        String name = input.readString();
        LocalDate date = kryo.readObjectOrNull(input, LocalDate.class);
        Long inhabitans = input.readLong();
        List<City> nearcityObjects = kryo.readObjectOrNull(input, ArrayList.class);
        return new City(id, name, date, inhabitans, nearcityObjects);
    }
}
