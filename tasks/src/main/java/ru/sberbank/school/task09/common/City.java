package ru.sberbank.school.task09.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class City implements Serializable {
    private int id;
    private String cityName;
    private LocalDate foundDate;
    private long numberOfInhabitants;
    private List<City> nearCities;

    public City() {
        cityName = "";
        foundDate = LocalDate.now();
        nearCities = new ArrayList<>();
    }

    public City(int id, String cityName, LocalDate foundDate, long numberOfInhabitants) {
        this(id, cityName, foundDate, numberOfInhabitants, new ArrayList<>());
    }

    public City(int id, String cityName, LocalDate foundDate, long numberOfInhabitants, List<City> nearCities) {
        this.id = id;
        this.cityName = cityName;
        this.foundDate = foundDate;
        this.numberOfInhabitants = numberOfInhabitants;
        this.nearCities = nearCities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDate getFoundDate() {
        return foundDate;
    }

    public void setFoundDate(LocalDate foundDate) {
        this.foundDate = foundDate;
    }

    public long getNumberOfInhabitants() {
        return numberOfInhabitants;
    }

    public void setNumberOfInhabitants(long numberOfInhabitants) {
        this.numberOfInhabitants = numberOfInhabitants;
    }

    public List<City> getNearCities() {
        return nearCities;
    }

    public void setNearCities(List<City> nearCities) {
        this.nearCities = nearCities;
    }

    public void addCity(City nextOne) {
        nearCities.add(nextOne);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id == city.id &&
                Objects.equals(cityName, city.cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityName);
    }

    public boolean compare(City city) {
        return id == city.id &&
                numberOfInhabitants == city.numberOfInhabitants &&
                Objects.equals(cityName, city.cityName) &&
                Objects.equals(foundDate, city.foundDate) &&
                Objects.equals(nearCities, city.nearCities);
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", foundDate=" + foundDate +
                ", numberOfInhabitants=" + numberOfInhabitants +
                // ", nearCities=" + nearCities +
                '}';
    }
}
