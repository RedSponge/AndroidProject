package com.redsponge.mycoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.redsponge.mycoolapp.db.DatabaseHandler;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ProjectsAdapter listAdapter;
    private ListView listView;
    private DatabaseHandler databaseHandler;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = 1;

        listAdapter = new ProjectsAdapter(this, new ArrayList<Project>(), currentUser);

        databaseHandler = new DatabaseHandler(this);


        listView = findViewById(R.id.my_list);
        listView.setAdapter(listAdapter);

        queryProjects();
    }

    private void queryProjects() {
        listAdapter.clear();
        for (Project project : databaseHandler.getAllProjects(currentUser)) {
            listAdapter.add(project);
        }

        String welcome = String.format(getString(R.string.placeholder_welcome), databaseHandler.getUser(currentUser).getName());
        ((TextView) findViewById(R.id.welcome_message)).setText(welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryProjects();
    }

    public void enterSettings(View view) {
        Intent change = new Intent(this, SettingsActivity.class);
        this.startActivity(change);
    }


    public void newProject(View view) {
        Intent intent = new Intent(this, NewProjectActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}
