package ru.sberbank.school.task08.state;

import lombok.Value;

import java.io.Serializable;

@Value
public class GameObject implements Serializable {
    private Type type;
    private Status status;

    enum Type {
        BUILDING,
        ITEM,
        ENEMY,
        NPC
    }

    enum Status {
        SPAWNED,
        DESPAWNED,
        KILLED
    }
}
