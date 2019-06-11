package ru.sberbank.school.task08;

import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private final static String url = "D:\\";
    private final static String file = "def.txt";

    public static void main(String[] args) throws SaveGameException {


//        KryoSerializableManager man1 = new KryoSerializableManager(url);
        SerializableManager man1 = new SerializableManager(url);
//        JacksonSerializableManager man1 = new JacksonSerializableManager(url);

        List<GameObject> list = new ArrayList<>();

        list.add((GameObject) man1.createEntity(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.SPAWNED, 10L));
//        list.add((GameObject) man1.createEntity(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 1L));
//        list.add((GameObject) man1.createEntity(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.DESPAWNED, 1L));
        man1.initialize();
        MapState map = (MapState) man1.createSavable("Enemy-1", list);
        System.out.println(map.toString());
        man1.saveGame(file, map);
        System.out.println(man1.loadGame(file).toString());
    }
}
