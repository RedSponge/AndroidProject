package com.redsponge.mycoolapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.redsponge.mycoolapp.db.DatabaseHandler;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ProjectsAdapter listAdapter;
    private ListView listView;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAdapter = new ProjectsAdapter(this, new ArrayList<Project>());

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.addUser(new User("RedSponge", "12345"));
        databaseHandler.addUser(new User("BlueSponge", "54321"));

        databaseHandler.addProject(new Project("Random Project", "Random Description"));
        databaseHandler.linkProjectToUser(new Project(1, "Random Project", "Random Description"), databaseHandler.getUser("RedSponge"), true);

        System.out.println(databaseHandler.getUser(1));

        listView = findViewById(R.id.my_list);
        listView.setAdapter(listAdapter);

        queryProjects();
    }

    private void queryProjects() {
        for (Project project : databaseHandler.getAllProjects(1)) {
            listAdapter.add(project);
        }
    }

    public void buttonClicked(View view) {
        listAdapter.add(new Project(0, "Howdy", "Yay!"));
    }
}
