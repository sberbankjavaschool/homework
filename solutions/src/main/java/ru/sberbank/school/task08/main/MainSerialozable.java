package ru.sberbank.school.task08.main;

import ru.sberbank.school.task08.SaveGameException;
import ru.sberbank.school.task08.SerializableManager;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

public class MainSerialozable {
    public static void main(String[] args) throws SaveGameException {
        List<GameObject> list = new ArrayList<>();
        SerializableManager serializableManager = new SerializableManager("D:\\");

        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.KILLED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 5000L));

        Savable<GameObject> savable1 = serializableManager.createSavable("game", list);
        serializableManager.saveGame("game.json", savable1);

        Savable<GameObject> game = serializableManager.loadGame("game.json");
        System.out.println(game.toString());

        System.out.println(game.equals(savable1));
        System.out.println(game.toString());


    }
}
