package ru.sberbank.school.task08;

import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.NonNull;
import ru.sberbank.school.task08.state.InstantiatableEntity;
import ru.sberbank.school.task08.state.InstantiatableMapState;
import ru.sberbank.school.task08.state.Savable;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class SaveGameManager {
    final String filesDirectory;
    @Getter
    final SaveState state = new SaveState();

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
    public abstract void saveGame(String filename, Savable gameState) throws SaveGameException;

    public abstract Savable loadGame(String filename) throws SaveGameException;

    /**
     * Возвращайте ваш класс-наследник InstantiatableEntity.
     * Может меняться от реализации к реализации.
     */
    public abstract Class<? extends InstantiatableEntity> getInstantiatableEntityClass();

    /**
     * Возвращайте ваш класс-наследник InstantiatableMapState.
     * Может меняться от реализации к реализации.
     */
    public abstract Class<? extends InstantiatableMapState> getInstantiatableMapStateClass();

    /**
     * Хранит состояние файлов-сохранений.
     */
    static class SaveState {
        private String lastSaveGameFilename;
        private SortedSet<String> allSaveFiles = new TreeSet<>();

        public Set<String> listSaves() {
            return ImmutableSet.copyOf(allSaveFiles);
        }

        /**
         * Добавляет новое сохранение в стейт.
         *
         * @param name название файла сохранения.
         * @return true если такого имени ещё нет.
         */
        boolean addSave(@NonNull String name) {
            boolean notPresent = allSaveFiles.add(name);
            if (notPresent) {
                lastSaveGameFilename = name;
            }
            return notPresent;
        }

        /**
         * Удаляет сохранение из стейта.
         *
         * @param name название файла
         * @return true если такое сохранение было
         */
        boolean removeSave(@NonNull String name) {
            boolean present = allSaveFiles.remove(name);
            if (present && name.equals(lastSaveGameFilename)) {
                if (allSaveFiles.isEmpty()) {
                    lastSaveGameFilename = null;
                } else {
                    lastSaveGameFilename = allSaveFiles.last();
                }
            }
            return present;
        }
    }
}
