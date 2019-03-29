package com.redsponge.mycoolapp.user;

import com.redsponge.mycoolapp.login.LoginUtils;

import java.io.Serializable;

/**
 * Represents a user in the database
 */
public class User implements Serializable {

    public int id;
    public final String name;
    public final int password;

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

    public User(String name, String password) {
        this(0, name, password);
    }

    @Override
    public String toString() {
        return name;
    }
}
