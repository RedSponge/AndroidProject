package com.redsponge.mycoolapp.db;

public class Tables {

    public static final String TABLE_USERS =
            "TABLE users (\n" +
                "user_name TEXT UNIQUE NOT NULL,\n" +
                "user_password INTEGER NOT NULL,\n" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT \n" +
            ");\n";


    public static final String TABLE_PROJECTS =
            "TABLE projects (\n" +
                "proj_name TEXT NOT NULL,\n" +
                "proj_description TEXT NOT NULL,\n" +
                "proj_icon TEXT,\n" +
                "proj_id INTEGER PRIMARY KEY AUTOINCREMENT\n" +
            ");";


    public static final String TABLE_PROJECT_GROUPS =
            "TABLE project_groups (\n" +
                "proj_id INTEGER NOT NULL,\n" +
                "user_id INTEGER NOT NULL,\n" +
                "admin INTEGER NOT NULL DEFAULT 0,\n" +
                "FOREIGN KEY(proj_id) REFERENCES projects(proj_id),\n" +
                "FOREIGN KEY(user_id) REFERENCES users(user_id),\n" +
                "PRIMARY KEY(proj_id, user_id)" +
            ");";


    public static final String TABLE_INVITES =
            "TABLE invites (\n" +
                "proj_id INTEGER NOT NULL,\n" +
                "from_id INTEGER NOT NULL,\n" +
                "to_id INTEGER NOT NULL CHECK(to_id <> from_id),\n" +
                "should_be_admin INTEGER NOT NULL DEFAULT 0,\n" +
                "PRIMARY KEY(proj_id, from_id, to_id),\n" +
                "FOREIGN KEY (proj_id) REFERENCES projects(proj_id),\n" +
                "FOREIGN KEY (from_id) REFERENCES users(user_id),\n" +
                "FOREIGN KEY (to_id) REFERENCES users(user_id)\n" +
            ");";


}
