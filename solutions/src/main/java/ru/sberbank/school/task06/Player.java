package ru.sberbank.school.task06;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class Player {
    private String name;
    private String team;
    private int age;

    public Player(Player p) {
        this.name = p.getName();
        this.team = p.getTeam();
        this.age = p.getAge();
    }

}
