package com.redsponge.mycoolapp;

import android.util.Log;

import java.io.Serializable;

public class User implements Serializable {

    public int id;
    public final String name;
    private final int pw;

    public User(int id, String name, int pw) {
        this.id = id;
        this.name = name;
        this.pw = pw; // Already Hashed
    }

    public User(int id, String name, String pw) {
        this.id = id;
        this.name = name;
        this.pw = LoginUtils.hashPw(pw);
        Log.i("User", "Hashed Password Is " + this.pw);
    }

    public User(String name, String pw) {
        this(0, name, pw);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", id, name, pw);
    }

    public int getPassword() {
        return pw;
    }
}
