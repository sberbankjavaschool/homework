package ru.sberbank.school.task09;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.asList;

public abstract class RouteService<C extends City, T extends Route> {
    private final Set<String> cities = new HashSet<>(asList(
            "Saint-Petersburg", "Moscow", "Chelyabinsk", "Berlin",
            "Sverdlovsk", "Murmansk", "Vladimir", "London", "Kiev",
            "San-Francisco", "Palo Alto", "New-York", "Rome",
            "Minsk", "Astana", "Vladivostok", "Novosibirsk"));
    protected CachePathProvider pathProvider;
    private int idCounter = 0;
    private SecureRandom rand = new SecureRandom();

    public RouteService(CachePathProvider pathProvider) {
        this.pathProvider = pathProvider;
    }

    /*
     * Медленный, неэффективный, и, возможно, расположенный в другой стране / на другой планете, сервис.
     * Главное, что присутствуют циклические зависимости.
     *
     * @param from Название города отправления
     * @param to Название города назначения
     * @return Готовый маршрут
     */
    public T getRoute(String from, String to) {
        checkNames(from, to);
        List<String> names = new ArrayList<>(cities);
        Collections.shuffle(names);

        List<C> generated = new ArrayList<>(names.size());

        names.forEach(s -> generated.add(generateCity(s)));
        generated.forEach(c -> addRandomCities(c, generated));

        generated.sort(getFirstToLastComparator(from, to));

        return createRoute(generated);
    }

    private Comparator<C> getFirstToLastComparator(String from, String to) {
        return (o1, o2) -> {
            if (o1.getCityName().equals(from) || o2.getCityName().equals(to)) {
                return -1;
            } else if (o1.getCityName().equals(to) || o2.getCityName().equals(from)) {
                return 1;
            }
            return 0;
        };
    }

    private void addRandomCities(C city, List<C> source) {
        do {
            C next = source.get(rand.nextInt(source.size()));
            if (!city.equals(next) && !city.getNearCities().contains(next)) {
                city.addCity(next);
            }
        } while (city.getNearCities().size() < 3);
    }

    private C generateCity(String name) {
        LocalDate date = LocalDate.of(100 + rand.nextInt(1900), 1 + rand.nextInt(11), 1 + rand.nextInt(27));
        return createCity(idCounter++, name, date, 6 * rand.nextInt(1000000));
    }

    /**
     * Создает город.
     *
     * @param id                  Уникальный идентификатор города
     * @param cityName            Название
     * @param foundDate           Дата основания
     * @param numberOfInhabitants Число жителей
     * @return новую сущность города
     */
    protected abstract C createCity(int id, String cityName, LocalDate foundDate, long numberOfInhabitants);

    /**
     * Создает маршрут.
     *
     * @param cities Список всех сгенерированных городов
     * @return готовый маршрут
     */
    protected abstract T createRoute(List<C> cities);

    private void checkNames(String from, String to) {
        checkName(from);
        checkName(to);
    }

    private void checkName(String name) {
        if (!cities.contains(name)) {
            throw new UnknownCityException(name);
        }
    }
}
