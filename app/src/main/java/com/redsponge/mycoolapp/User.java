package com.redsponge.mycoolapp;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private final String name;
    private final String pw;

    public User(int id, String name, String pw) {
        this.id = id;
        this.name = name;
        this.pw = pw;
    }

    public User(String name, String pw) {
        this(0, name, pw);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", id, name, pw);
    }

    public String getPassword() {
        return pw;
    }
}
