package ru.sberbank.school.task08;

import lombok.NonNull;
import ru.sberbank.school.task08.state.*;
import ru.sberbank.school.util.Solution;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.List;
import java.util.Objects;

@Solution(8)
public class JaxbManager extends SaveGameManager<JaxbMapState<GameObject>, GameObject> {

    /**
     * Класс-наследник должен иметь конструктор эквивалентный этому.
     *
     * @param filesDirectoryPath Путь до директории, в которой хранятся файлы сохранений
     */
    public JaxbManager(@NonNull String filesDirectoryPath) {
        super(filesDirectoryPath);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void saveGame(String filename, JaxbMapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        Objects.requireNonNull(gameState, "Сохраняемый объект не должен быть null");

        try {
            JAXBContext context = JAXBContext.newInstance(gameState.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(gameState, new File(filesDirectory + File.separator + filename));

        } catch (JAXBException e) {
            throw new SaveGameException("Возникла ошибка при записи объекта", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public JaxbMapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        JaxbMapState savable = null;

        try {
            JAXBContext context = JAXBContext.newInstance(JaxbMapState.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            savable = (JaxbMapState) unmarshaller.unmarshal(
                    new File(filesDirectory + File.separator + filename));
        } catch (JAXBException e) {
            throw new SaveGameException("Возникла ошибка при считывании объекта", e,
                    SaveGameException.Type.IO, savable);
        }

        return savable;
    }

    @Override
    public GameObject createEntity(InstantiatableEntity.Type type,
                                             InstantiatableEntity.Status status,
                                             long hitPoints) {
//        Objects.requireNonNull(type, "Тип не должен быть null");
//        Objects.requireNonNull(status, "Статус не должен быть null");
//        if (hitPoints < 0) {
//            throw new IllegalArgumentException("Урон не может быть меньше 0");
//        }

        return new GameObject(type, status, hitPoints);
    }

    @Override
    public JaxbMapState<GameObject> createSavable(String name, List<GameObject> entities) {
//        Objects.requireNonNull(name, "Название локации не должено быть null");
//        Objects.requireNonNull(entities, "Список объектов не должен быть null");

        return new JaxbMapState<>(name, entities);
    }

}
