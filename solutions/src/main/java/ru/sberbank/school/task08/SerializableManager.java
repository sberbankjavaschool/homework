package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.Savable;

public class SerializableManager extends SaveGameManager {

    public SerializableManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public Class<? extends Savable> getGameStateClass() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void saveGame(String filename, Savable gameState) throws SaveGameException {

    }

    @Override
    public Savable loadGame(String filename) throws SaveGameException {
        return null;
    }
}
