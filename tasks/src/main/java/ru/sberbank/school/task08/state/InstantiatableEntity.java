package ru.sberbank.school.task08.state;

public interface InstantiatableEntity {

    InstantiatableEntity getInstance(Type type, Status status, long hitPoints);

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
