package ru.sberbank.school.task08.state;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class MapState<T extends InstantiatableEntity> implements Savable<T>, Serializable {
    private String name;
    private List<T> gameObjects;

    public MapState() {

    }

    public MapState(String name, List<T> gameObjects) {
        this.name = name;
        this.gameObjects = gameObjects;
    }

    public String getName() {
        return name;
    }

    public List<T> getGameObjects() {
        return this.gameObjects;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MapState)) {
            return false;
        }
        final MapState other = (MapState) o;
        final Object this$gameObjects = this.getGameObjects();
        final Object other$gameObjects = other.getGameObjects();
        return Objects.equals(this$gameObjects, other$gameObjects);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $gameObjects = this.getGameObjects();
        result = result * PRIME + ($gameObjects == null ? 43 : $gameObjects.hashCode());
        return result;
    }

    public String toString() {
        return "MapState(gameObjects=" + this.getGameObjects() + ")";
    }
}