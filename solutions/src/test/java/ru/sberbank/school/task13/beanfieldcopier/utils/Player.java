package ru.sberbank.school.task13.beanfieldcopier.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Player {
    private String name;
    private String team;
    private int age;
    private String illnes;

    public Player() {

    }

    public Player(int age, String name, String team, String illnes) {
        this.name = name;
        this.team = team;
        this.age = age;
        this.illnes = illnes;
    }

    public Player(Player p) {
        this.name = p.getName();
        this.team = p.getTeam();
        this.age = p.getAge();
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public int getAge() {
        return age;
    }

    public String getIllnes() {
        return illnes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private void setIllnes(String illnes) {
        this.illnes = illnes;
    }

    public String toStringAllFields() {
        return name + " " + age + " " + team + " " + illnes;
    }

    public String toString() {
        return name + " " + age + " " + team;
    }

}
