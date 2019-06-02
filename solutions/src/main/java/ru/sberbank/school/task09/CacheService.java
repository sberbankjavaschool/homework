package ru.sberbank.school.task09;

import java.util.Objects;
import java.util.Optional;

public abstract class CacheService {
    final String filesDirectory;

    public CacheService(String filesDirectoryPath) {
        Objects.requireNonNull(filesDirectoryPath);
        this.filesDirectory = filesDirectoryPath;
    }

    public abstract void save(String filename, Route<City> route);

    public abstract Optional<Route<City>> load(String filename);
}
