package ru.sberbank.school.task08.main;

import ru.sberbank.school.task08.JacksonSerializeableManager;
import ru.sberbank.school.task08.SaveGameException;
import ru.sberbank.school.task08.state.GameObject;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.MapState;
import ru.sberbank.school.task08.state.Savable;

import java.util.ArrayList;
import java.util.List;

public class MainJackson {
    public static void main(String[] args) throws SaveGameException {
        JacksonSerializeableManager jackson = new  JacksonSerializeableManager("D:\\");
        List<GameObject> list = new ArrayList<>();
        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ITEM, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.SPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.BUILDING, InstantiatableEntity.Status.KILLED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.NPC, InstantiatableEntity.Status.DESPAWNED, 5000L));
        list.add(new GameObject(InstantiatableEntity.Type.ENEMY, InstantiatableEntity.Status.KILLED, 5000L));
        MapState savable = jackson.createSavable("game", null);
        jackson.initialize();
        jackson.saveGame("game.json", savable);
        MapState game = jackson.loadGame("game.json");
        jackson.saveGame("game1.json", game);
        System.out.println(savable.equals(game));
    }
}
