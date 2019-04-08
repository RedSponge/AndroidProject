package com.redsponge.mycoolapp.user;

import com.redsponge.mycoolapp.login.LoginUtils;

import java.io.Serializable;

/**
 * Represents a user in the database
 */
public class User implements Serializable {

    private int id;
    private String name;
    private int password;

    public User(int id, String name, int password) {
        this.id = id;
        this.name = name;
        this.password = password; // Already Hashed
    }

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = LoginUtils.hashPw(password);
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

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public User(String name, String password) {
        this(0, name, password);
    }

    @Override
    public String toString() {
        return name;
    }
}
