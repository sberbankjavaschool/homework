package ru.sberbank.school.task08;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;

public class KryoSerializer extends Serializer<MapState<GameObject>> {
    @Override
    public void write(Kryo kryo, Output output, MapState<GameObject> object) {
        output.writeString(object.getName());
        kryo.writeObjectOrNull(output, object.getGameObjects(), ArrayList.class);
    }

    @Override
    public MapState<GameObject> read(Kryo kryo, Input input, Class<? extends MapState<GameObject>> type) {
        String name = input.readString();
        List<GameObject> list = (List<GameObject>) kryo.readObjectOrNull(input, ArrayList.class);
        return new MapState<>(name, list);
    }
}
