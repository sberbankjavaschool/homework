package ru.sberbank.school.task08.state;

public interface Savable {

    InstantiatableMapState getMapState();

    <T extends InstantiatableMapState> void setMapState(T mapState);
}
