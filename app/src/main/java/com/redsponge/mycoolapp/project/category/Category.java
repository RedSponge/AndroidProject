package com.redsponge.mycoolapp.project.category;

import com.redsponge.mycoolapp.db.DatabaseHandler;

/**
 * A category, which is used to sort projects into
 * A project may be in many categories. each user has their own individual categories.
 */
public class Category {

    private String name;
    private int user;
    private int id;

    public Category(int id, String name, int user) {
        this.name = name;
        this.user = user;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public Category(String name, int user) {
        this(0, name, user);
    }

    @Override
    public String toString() {
        return name + " (" + DatabaseHandler.getInstance().getProjectAmountInCategory(id, user) + ")";
    }
}
