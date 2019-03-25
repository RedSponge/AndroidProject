package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.login.LoginUtils;
import com.redsponge.mycoolapp.utils.SettingsActivity;
import com.redsponge.mycoolapp.db.DatabaseHandler;

import java.util.ArrayList;

public class ProjectViewActivity extends Activity {

    private ProjectsAdapter listAdapter;
    private ListView listView;
    private DatabaseHandler databaseHandler;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_view);

        currentUser = getIntent().getExtras().getInt("currentUser");
        LoginUtils.registerCurrentUser(this, currentUser);

        listAdapter = new ProjectsAdapter(this, new ArrayList<Project>(), currentUser);

        databaseHandler = new DatabaseHandler(this);


        listView = (ListView) findViewById(R.id.my_list);
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
