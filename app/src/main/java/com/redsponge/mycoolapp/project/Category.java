package com.redsponge.mycoolapp.project;

public class Category {

    public final String name;
    public final int user;
    public final int id;

    public Category(int id, String name, int user) {
        this.name = name;
        this.user = user;
        this.id = id;
    }

    public Category(String name, int user) {
        this(0, name, user);
    }
}
