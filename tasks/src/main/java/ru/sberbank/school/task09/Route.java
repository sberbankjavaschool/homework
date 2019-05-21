package ru.sberbank.school.task09;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Route<C extends City> {
    private String routeName;
    private List<C> cities = new LinkedList<>();

    public Route() {

    }

    public Route(String routeName, List<C> cities) {
        this.routeName = routeName;
        this.cities = cities;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public List<C> getCities() {
        return cities;
    }

    public void setCities(List<C> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Route: { " +
                cities.stream()
                        .map(City::getCityName)
                        .collect(Collectors.joining(" -> "))
                + " }";
    }
}
