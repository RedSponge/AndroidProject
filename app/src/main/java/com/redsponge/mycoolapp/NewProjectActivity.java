package com.redsponge.mycoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.db.DatabaseHandler;

public class NewProjectActivity extends Activity {

    private EditText nameInput;
    private DatabaseHandler dbHandler;

    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        nameInput = findViewById(R.id.projectNameInput);
        dbHandler = new DatabaseHandler(this);

        currentUser = getIntent().getExtras().getInt("currentUser", -1);
        if(currentUser == -1) {
            throw new RuntimeException("Current User Wasn't Passed!");
        }
    }

    public void createProject(View view) {
        String name = nameInput.getText().toString();
        String description = "";

        if(name.trim().equals("")) {
            return;
        }

        Project newProj = new Project(name, description);

        int id = dbHandler.addProject(newProj);

        dbHandler.linkProjectToUser(id, currentUser, true);

        this.finish(); // Closes this activity
    }
}
