package com.redsponge.mycoolapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.redsponge.mycoolapp.Project;
import com.redsponge.mycoolapp.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "projects.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (\n" +
                "    user_name TEXT UNIQUE NOT NULL,\n" +
                "    user_password INTEGER NOT NULL,\n" +
                "    user_id INTEGER PRIMARY KEY AUTOINCREMENT \n" +
                ");\n");
        db.execSQL("CREATE TABLE projects (\n" +
                "    proj_name TEXT NOT NULL,\n" +
                "    proj_description TEXT NOT NULL,\n" +
                "    proj_id INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                ");");
        db.execSQL("CREATE TABLE project_groups (\n" +
                "    proj_id INTEGER NOT NULL,\n" +
                "    user_id INTEGER NOT NULL,\n" +
                "    admin INTEGER NOT NULL DEFAULT 0,\n" +
                "    FOREIGN KEY(proj_id) REFERENCES projects(proj_id),\n" +
                "    FOREIGN KEY(user_id) REFERENCES users(user_id),\n" +
                "    PRIMARY KEY(proj_id, user_id)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS projects");
        db.execSQL("DROP TABLE IF EXISTS project_groups");
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS projects");
        db.execSQL("DROP TABLE IF EXISTS project_groups");
        onCreate(db);
    }

    public User getUser(int id) {
        final Cursor cursor = getReadableDatabase().rawQuery("SELECT user_id, user_name, user_password FROM users WHERE user_id = " + id, null);
        final User user;

        if(cursor.moveToFirst()) {
            user = new User(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
        } else {
            user = null;
        }
        cursor.close();

        return user;
    }

    /**
     * Finds a user by name.
     * @param name The user's name
     * @return The user's id. -1 if the user doesn't exist.
     */
    public int getUserId(String name) {
        final Cursor cursor = getReadableDatabase().rawQuery("SELECT user_id FROM users WHERE user_name = \"" + name + "\"", null);
        int id = -1;
        if(cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    public User getUser(String name) {
        return getUser(getUserId(name));
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL(String.format("INSERT INTO users (user_name, user_password) VALUES(\"%s\", %s)", user.getName(), user.getPassword()));
            Log.i(getClass().getName(), "Adding user: " + user);
        } catch (SQLiteConstraintException e) {
            Log.e(getClass().getName(), "Constraint Exception! Might be user name!", e);
        }
        db.close();
    }

    public List<Project> getAllProjects(int user) {
        ArrayList<Project> projects = new ArrayList<>();

        final Cursor cursor = getReadableDatabase().rawQuery("SELECT projects.proj_id, projects.proj_name, projects.proj_description FROM users " +
                "INNER JOIN project_groups, projects on users.user_id = project_groups.user_id AND projects.proj_id = project_groups.proj_id " +
                        "WHERE users.user_id = " + user, null);

        while(cursor.moveToNext()) {
            projects.add(new Project(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
        }

        cursor.close();
        return projects;
    }

    /**
     * Adds a new project to the database
     * @param project The project to add
     * @return The new project's id
     */
    public int addProject(Project project) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("INSERT INTO projects (proj_name, proj_description) VALUES(\"%s\", \"%s\")", project.name, project.description));
        Cursor c = db.rawQuery("SELECT proj_id FROM projects ORDER BY proj_id DESC", null);

        c.moveToFirst();
        int id = c.getInt(0);
        c.close();

        Log.i(getClass().getName(), "New project id is " + id);

        db.close();

        return id;
    }

    public void linkProjectToUser(Project project, User user, boolean isAdmin) {
        linkProjectToUser(project.id, user.id, isAdmin);
    }

    public void linkProjectToUser(int project, int user, boolean isAdmin) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format("INSERT INTO project_groups (user_id, proj_id, admin) VALUES(%s, %s, %s)", user, project, isAdmin ? 1 : 0));
        db.close();
    }

    public void restart() {
        onUpgrade(getWritableDatabase(), 0, 0);
    }

    public void deleteProject(Project project) {
        deleteProject(project.id);
    }

    public void deleteProject(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM projects WHERE proj_id = " + id);
    }

    public boolean isUserAdmin(int currentUser, int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users INNER JOIN projects, project_groups\n" +
                "ON  project_groups.proj_id = projects.proj_id\n" +
                "AND project_groups.user_id = users.user_id\n" +
                "AND project_groups.admin = 1 AND projects.proj_id = " + id + " " +
                "AND users.user_id = " + currentUser, null);
        boolean isAdmin;

        if(cursor.getCount() > 1) {
            throw new RuntimeException("Shouldn't be more than one! " + cursor.getCount());
        } else {
            isAdmin = cursor.getCount() == 1;
        }
        cursor.close();
        db.close();
        return isAdmin;
    }
}
