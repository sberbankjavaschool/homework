package ru.sberbank.school.task09;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import lombok.NonNull;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

public class CityKryoSerializer extends Serializer<City> {


    @Override
    public void write(Kryo kryo, Output output, @NonNull City object) {

    }

    @Override
    public City read(Kryo kryo, Input input, Class<? extends City> type) {
        return null;
    }
}
