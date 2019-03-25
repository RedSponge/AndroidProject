package com.redsponge.mycoolapp.project;

import java.io.Serializable;

public class Project implements Serializable {

    public int id;
    public String name;
    public String description;

    public Project(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = -1;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + description;
    }
}
