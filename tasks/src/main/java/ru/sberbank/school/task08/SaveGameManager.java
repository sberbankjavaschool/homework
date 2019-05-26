package ru.sberbank.school.task08;

import java.util.List;

import lombok.NonNull;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.Savable;

public abstract class SaveGameManager<T extends Savable<R>, R extends InstantiatableEntity> {
    final String filesDirectory;

    /**
     * Класс-наследник должен иметь конструктор эквивалентный этому.
     *
     * @param filesDirectoryPath Путь до директории, в которой хранятся файлы сохранений
     */
    public SaveGameManager(@NonNull String filesDirectoryPath) {
        this.filesDirectory = filesDirectoryPath;
    }

    /**
     * Инициализацию класса можете осуществить в этом методе.
     * Он гарантированно будет вызван перед вызовом других методов класса.
     */
    public abstract void initialize();

    /**
     * Сохраняет в файл.
     *
     * @param filename  имя файла
     * @param gameState состояние, которое нужно сохранить
     */
    public abstract void saveGame(String filename, T gameState) throws SaveGameException;

    /**
     * Читает из файла.
     *
     * @param filename имя файла
     * @return загруженное состояние
     */
    public abstract T loadGame(String filename) throws SaveGameException;

    public abstract InstantiatableEntity createEntity(InstantiatableEntity.Type type,
                                                      InstantiatableEntity.Status status,
                                                      long hitPoints);

    public abstract T createSavable(String name, List<R> entities);
}
