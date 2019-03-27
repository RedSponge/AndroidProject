package com.redsponge.mycoolapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.redsponge.mycoolapp.project.Invite;
import com.redsponge.mycoolapp.project.Project;
import com.redsponge.mycoolapp.utils.User;

import java.util.ArrayList;
import java.util.List;

// TODO NAMES ARE CASE INSENSITIVE!!

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "projects.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE " + Tables.TABLE_USERS);
        db.execSQL("CREATE " + Tables.TABLE_PROJECTS);
        db.execSQL("CREATE " + Tables.TABLE_PROJECT_GROUPS);
        db.execSQL("CREATE " + Tables.TABLE_INVITES);
    }

    public void deleteTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS projects");
        db.execSQL("DROP TABLE IF EXISTS project_groups");
        db.execSQL("DROP TABLE IF EXISTS invites");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        deleteTables(db);
        onCreate(db);
    }

    /**
     * Finds a user by id
     * @param id The user's id
     * @return The user
     */
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
     * Finds a user by name
     * @param name The user's name
     * @return The user
     */
    public User getUser(String name) {
        final Cursor cursor = getReadableDatabase().rawQuery("SELECT user_id, user_name, user_password FROM users WHERE user_name = ?", new String[]{name});
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
     * Adds a user to the database
     * @param user The user to add
     * @return The user's new id
     */
    public int addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        // Create user
        db.execSQL("INSERT INTO users (user_name, user_password) VALUES(?, ?)",new Object[] {user.getName(), user.getPassword()});

        // Fetch new id
        Cursor c = db.rawQuery("SELECT user_id FROM users ORDER BY user_id DESC", null);
        c.moveToFirst();
        int id = c.getInt(0);
        c.close();
        db.close();

        return id;
    }

    /**
     * Gets all the projects of a user
     * @param user The user to get all the projects of
     * @return The projects of the user
     */
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

        // Create project
        db.execSQL("INSERT INTO projects (proj_name, proj_description) VALUES(?, ?)", new Object[] {project.name, project.description});

        // Fetch new id
        Cursor c = db.rawQuery("SELECT proj_id FROM projects ORDER BY proj_id DESC", null);
        c.moveToFirst();
        int id = c.getInt(0);
        c.close();

        db.close();

        return id;
    }

    /**
     * Links a project to a user
     * @param project The project to link
     * @param user The user to link
     * @param isAdmin Should this user be registered as admin
     */
    public void linkProjectToUser(int project, int user, boolean isAdmin) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO project_groups (user_id, proj_id, admin) VALUES(?, ?, ?)", new Object[] {user, project, isAdmin ? 1 : 0});
        db.close();
    }

    /**
     * Restarts the database
     */
    public void restart() {
        SQLiteDatabase db = getWritableDatabase();
        deleteTables(db);
        onCreate(db);
    }

    /**
     * Removes the project and all of it's links
     * @param id The project's id
     */
    public void deleteProject(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM projects WHERE proj_id = " + id);
        db.execSQL("DELETE FROM project_groups WHERE proj_id = " + id);
        db.close();
    }

    /**
     * Checks if a user is an admin on a project
     * @param user The user's id
     * @param project The project's id
     * @return Is the user an admin
     */
    public boolean isUserAdmin(int user, int project) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users INNER JOIN projects, project_groups\n" +
                "ON  project_groups.proj_id = projects.proj_id\n" +
                "AND project_groups.user_id = users.user_id\n" +
                "AND project_groups.admin = 1 AND projects.proj_id = " + project + " " +
                "AND users.user_id = \"" + user + "\"", null);
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

    public void updateProjectDescription(int id, String description) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE projects SET proj_description = ? WHERE proj_id = ?", new Object[] {description, id});
        db.close();
    }

    /**
     * Adds a new invite to a project
     * @param invite The invite
     */
    public void addInvite(Invite invite) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO invites (from_id, to_id, proj_id, should_be_admin) VALUES (?, ?, ?, ?)", new Object[] {invite.idFrom, invite.idTo, invite.projectId, invite.shouldBeAdmin});
        db.close();
    }

    /**
     * Fetch all the invites for a user
     * @param user The user to fetch the invites for
     * @return The invites
     */
    public List<Invite> getInvites(int user) {
        ArrayList<Invite> invites = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT from_id, to_id, proj_id, should_be_admin FROM invites WHERE to_id = " + user, null);

        while(c.moveToNext()) {
            invites.add(new Invite(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3)));
        }

        c.close();
        db.close();

        return invites;
    }

    public int getInviteCount(int user) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(to_id) FROM invites WHERE to_id = " + user + " GROUP BY to_id", null);
        int count = 0;
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        db.close();
        return count;
    }

    /**
     * Checks if a user is already invited to a project
     * @param user The user to check
     * @param project The project to check
     * @return Is the user already invited to the project
     */
    public boolean isInvited(int user, int project) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM invites WHERE to_id = " + user + " AND proj_id = " + project, null);

        int count = c.getCount();

        c.close();

        return count == 1;
    }

    /**
     * Fetches a project from the database
     * @param projectId The project's id
     * @return The project
     */
    public Project getProject(int projectId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT proj_id, proj_name, proj_description FROM projects WHERE proj_id = " + projectId, null);
        Project p = null;

        if(c.moveToFirst()) {
            p = new Project(c.getInt(0), c.getString(1), c.getString(2));
        }

        c.close();
        db.close();
        return p;
    }

    /**
     * Removes an invitation from the database
     * @param invite The invitation to remove
     */
    public void removeInvite(Invite invite) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM invites WHERE from_id = ? AND to_id = ? AND proj_id = ?", new Object[] {invite.idFrom, invite.idTo, invite.projectId});
        db.close();
    }

    /**
     * Checks if a user is linked to a project
     * @param user The user to check
     * @param project The project to check
     * @return Is the user linked to the project
     */
    public boolean isPartOfProject(int user, int project) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM project_groups WHERE proj_id = " + project + " AND user_id = " + user, null);
        boolean isPart = c.getCount() == 1;
        c.close();
        db.close();
        return isPart;
    }

    public String getIcon(int project) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT proj_icon FROM projects WHERE proj_id = " + project, null);
        String icon = null;

        if(c.moveToFirst()) {
            icon = c.getString(0);
        }
        c.close();
        db.close();

        return icon;
    }

    public void setProjectIcon(int id, String base64) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE projects SET proj_icon = ? WHERE proj_id = ?", new Object[] {base64, id});
    }
}
