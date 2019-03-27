package com.redsponge.mycoolapp.utils;

import android.util.Log;

import com.redsponge.mycoolapp.login.LoginUtils;

import java.io.Serializable;

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
        Log.i("User", "Hashed Password Is " + this.password);
    }

    public User(String name, String password) {
        this(0, name, password);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", id, name, password);
    }
}
