package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import java.util.List;

@Solution(8)
public class SerializableManager extends SaveGameManager<MapState<GameObject>, GameObject> {
    /**
     * Конструктор не меняйте.
     */
    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public void saveGame(@NonNull String filename, @NonNull MapState<GameObject> gameState)
        throws SaveGameException {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public MapState<GameObject> loadGame(String filename) throws SaveGameException {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
        return new GameObject(type, status, hitPoints);
    }

    @Override
    public MapState<GameObject> createSavable(String name, List<GameObject> entities) {
        return new MapState<>(name, entities);
    }

}
