package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

@Solution(8)
public class SerializableManager extends SaveGameManager {
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
    public void saveGame(String filename, Savable gameState) throws SaveGameException {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public Savable loadGame(String filename) throws SaveGameException {
        throw new UnsupportedOperationException("Implement me!");
    }

    @Override
    public Class<? extends InstantiatableEntity> getInstantiatableEntityClass() {
        return GameObject.class;
    }

    @Override
    public Class<? extends InstantiatableMapState> getInstantiatableMapStateClass() {
        return MapState.class;
    }
}
