package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;

public class NewProjectActivity extends Activity {

    private EditText nameInput;
    private EditText descriptionInput;

    private DatabaseHandler dbHandler;

    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        nameInput = (EditText) findViewById(R.id.projectNameInput);
        descriptionInput = (EditText) findViewById(R.id.projectDescriptionInput);

        dbHandler = new DatabaseHandler(this);

        currentUser = getIntent().getExtras().getInt("currentUser", -1);
        if(currentUser == -1) {
            throw new RuntimeException("Current User Wasn't Passed!");
        }

        nameInput.setText("New Project!");
        descriptionInput.setText("A Masterpiece");

        nameInput.requestFocus();
    }

    public void createProject(View view) {
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();

        if(name.trim().equals("")) {
            return;
        }

        Project newProj = new Project(name, description);

        int id = dbHandler.addProject(newProj);

        dbHandler.linkProjectToUser(id, currentUser, true);

        this.finish(); // Closes this activity
    }
}
