package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.login.LoginActivity;
import com.redsponge.mycoolapp.login.LoginUtils;
import com.redsponge.mycoolapp.utils.SettingsActivity;

import java.util.ArrayList;

public class ProjectViewActivity extends Activity {

    private ProjectsAdapter listAdapter;
    private DatabaseHandler db;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        currentUser = getIntent().getExtras().getInt("currentUser");

        db = new DatabaseHandler(this);
        listAdapter = new ProjectsAdapter(this, new ArrayList<Project>(), currentUser, db);

        if(db.getUser(currentUser) == null) {
            logout(null);
        }

        ListView listView = (ListView) findViewById(R.id.projectViewList);
        listView.setAdapter(listAdapter);

        queryProjects();
        updateInvitesButton();
    }

    /**
     * Updates the {@link ProjectViewActivity#listAdapter} based on the projects from the database
     */
    private void queryProjects() {
        listAdapter.clear();
        for (Project project : db.getAllProjects(currentUser)) {
            listAdapter.add(project);
        }

        String welcome = String.format(getString(R.string.placeholder_welcome), db.getUser(currentUser).name);
        ((TextView) findViewById(R.id.welcome_message)).setText(welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryProjects();
        updateInvitesButton();
    }

    /**
     * Updates the count on the invites button
     */
    private void updateInvitesButton() {
        Button viewInvitesButton = (Button) findViewById(R.id.viewInvitesButton);
        viewInvitesButton.setText(String.format(getString(R.string.view_invites), db.getInviteCount(currentUser)));
    }

    /**
     * Enters the settings screen ({@link SettingsActivity})
     */
    public void enterSettings(View view) {
        Intent change = new Intent(this, SettingsActivity.class);
        change.putExtra("currentUser", currentUser);
        this.startActivity(change);
    }

    /**
     * Enters the new project screen ({@link NewProjectActivity})
     */
    public void newProject(View view) {
        Intent intent = new Intent(this, NewProjectActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }

    /**
     * Logs out from a user, removing it from the {@link android.content.SharedPreferences}
     */
    public void logout(View view) {
        LoginUtils.clearCurrentUser(this);
        Intent backToLogin = new Intent(this, LoginActivity.class);
        finish();
        startActivity(backToLogin);
    }

    /**
     * Called when the view invites button is pressed
     */
    public void viewInvites(View view) {
        Log.i(getClass().getName(), "Viewing invites!");
        Intent i = new Intent(this, InviteViewActivity.class);
        i.putExtra("currentUser", currentUser);
        startActivity(i);
    }
}