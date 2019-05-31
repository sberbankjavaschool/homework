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
    private JAXBContext context;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

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
        try {
            context = JAXBContext.newInstance(JaxbMapState.class);
            marshaller = context.createMarshaller();
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveGame(String filename, JaxbMapState<GameObject> gameState) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        Objects.requireNonNull(gameState, "Сохраняемый объект не должен быть null");
        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(gameState, new File(filesDirectory + File.separator + filename));
        } catch (NullPointerException | JAXBException e) {
            throw new SaveGameException("Возникла ошибка при записи объекта", e, SaveGameException.Type.IO, gameState);
        }
    }

    @Override
    public JaxbMapState<GameObject> loadGame(String filename) throws SaveGameException {
        Objects.requireNonNull(filename, "Имя файла не должно быть null");
        JaxbMapState savable = null;

        try {

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

        return new GameObject(type, status, hitPoints);
    }

    @Override
    public JaxbMapState<GameObject> createSavable(String name, List<GameObject> entities) {

        return new JaxbMapState<>(name, entities);
    }

}
