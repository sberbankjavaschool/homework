package ru.sberbank.school.task08.state;

public interface InstantiatableEntity {

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
