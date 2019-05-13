package com.redsponge.mycoolapp.db;

public class Tables {

    public static class Users {
        public static final String NAME = "users";
        public static final String DECLARATION =
                "TABLE users (\n" +
                    "user_name TEXT UNIQUE NOT NULL,\n" +
                    "user_password INTEGER NOT NULL,\n" +
                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT \n" +
                ");\n";
    }

    public static class Projects {
        public static final String NAME = "projects";
        public static final String DECLARATION =
                "TABLE projects (\n" +
                    "proj_name TEXT NOT NULL,\n" +
                    "proj_description TEXT NOT NULL,\n" +
                    "proj_icon TEXT,\n" +
                    "proj_id INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");";
    }

    public static class ProjectGroups {
        public static final String NAME = "project_groups";
        public static final String DECLARATION =
                "TABLE project_groups (\n" +
                    "proj_id INTEGER NOT NULL,\n" +
                    "user_id INTEGER NOT NULL,\n" +
                    "admin INTEGER NOT NULL DEFAULT 0,\n" +
                    "FOREIGN KEY(proj_id) REFERENCES projects(proj_id),\n" +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id),\n" +
                    "PRIMARY KEY(proj_id, user_id)" +
                ");";
    }

    public static class Invites {
        public static final String NAME = "invites";
        public static final String DECLARATION =
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

    public static class Categories {
        public static final String NAME = "categories";
        public static final String DECLARATION =
                "TABLE categories (\n" +
                    "category_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "category_name TEXT NOT NULL,\n" +
                    "user_id INTEGER,\n" +
                    "FOREIGN KEY(user_id) REFERENCES users(user_id)\n" +
                ")";
    }

    public static class CategoryLinks {
        public static final String NAME = "category_links";
        public static final String DECLARATION =
                "TABLE category_links (\n" +
                    "category_id INTEGER NOT NULL,\n" +
                    "proj_id INTEGER NOT NULL,\n" +
                    "FOREIGN KEY(category_id) REFERENCES categories(category_id),\n" +
                    "FOREIGN KEY(proj_id) REFERENCES projects(proj_id),\n" +
                    "PRIMARY KEY(category_id, proj_id)" +
                ")";
    }

    public static class Events {
        public static final String NAME = "events";
        public static final String DECLARATION =
                "TABLE events (\n" +
                "    event_name TEXT NOT NULL,\n" +
                "    event_time INTEGER NOT NULL,\n" +
                "    event_status INTEGER NOT NULL,\n" +
                "    event_project INTEGER NOT NULL,\n" +
                "    event_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    FOREIGN KEY (event_project) REFERENCES projects(proj_id)\n" +
                ")";
                
    }

}
