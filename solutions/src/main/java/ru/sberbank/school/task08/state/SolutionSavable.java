package ru.sberbank.school.task08.state;

public interface SolutionSavable extends Savable {
    @Override
    MapState getMapState();
}
