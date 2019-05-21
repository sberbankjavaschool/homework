package ru.sberbank.school.task08.state;

import java.util.List;

public interface InstantiatableMapState<T extends InstantiatableEntity> {

    InstantiatableMapState<T> getInstance(List<T> entities);
}
