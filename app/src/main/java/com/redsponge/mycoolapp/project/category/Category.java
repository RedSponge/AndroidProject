package com.redsponge.mycoolapp.project.category;

import com.redsponge.mycoolapp.db.DatabaseHandler;

/**
 * A category, which is used to sort projects into
 * A project may be in many categories - one per user
 */
public class Category {

    public final String name;
    public final int user;
    public final int id;

    private DatabaseHandler db;

    public Category(int id, String name, int user, DatabaseHandler db) {
        this.name = name;
        this.user = user;
        this.id = id;
        this.db = db;
    }

    public Category(String name, int user, DatabaseHandler db) {
        this(0, name, user, db);
    }

    @Override
    public String toString() {
        return name + " (" + db.getProjectAmountInCategory(id, user) + ")";
    }
}
