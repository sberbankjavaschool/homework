package ru.sberbank.school.task08.state;

import java.util.List;

// no setters, only getter.
public class MapState<T extends InstantiatableEntity> implements InstantiatableMapState<T> {
    private final String name;
    private final List<T> gameObjects;

    private MapState() {
        gameObjects = null;
        name = null;
    }

    public MapState(String name, List<T> gameObjects) {
        this.name = name;
        this.gameObjects = gameObjects;
    }

    @Override
    public InstantiatableMapState<T> getInstance(List<T> entities) {
        return new MapState<>(name, entities);
    }

    public List<T> getGameObjects() {
        return this.gameObjects;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MapState)) return false;
        final MapState other = (MapState) o;
        final Object this$gameObjects = this.getGameObjects();
        final Object other$gameObjects = other.getGameObjects();
        return this$gameObjects == null ? other$gameObjects == null : this$gameObjects.equals(other$gameObjects);
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
