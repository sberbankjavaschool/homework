package ru.sberbank.school.task08.state;

import lombok.Getter;

public interface InstantiatableEntity {

    @Getter
    enum Type {
        BUILDING(0),
        ITEM(1),
        ENEMY(2),
        NPC(3);
        private final byte code;

        Type(int code) {
            this.code = (byte) code;
        }
    }

    @Getter
    enum Status {
        SPAWNED(0),
        DESPAWNED(1),
        KILLED(2);
        private final byte code;

        Status(int code) {
            this.code = (byte) code;
        }
    }
}
