package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.utils.AbstractActivity;

public class NewProjectActivity extends AbstractActivity {

    private EditText nameInput;
    private EditText descriptionInput;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_new_project);

        nameInput = (EditText) findViewById(R.id.projectNameInput);
        descriptionInput = (EditText) findViewById(R.id.projectDescriptionInput);

        nameInput.setText(ProjectTemplates.getRandomName());
        descriptionInput.setText(ProjectTemplates.getRandomDescription());

        nameInput.requestFocus();
    }

    public void createProject(View view) {
        String name = nameInput.getText().toString();
        String description = descriptionInput.getText().toString();

        if(name.isEmpty()) {
            nameInput.setError(getString(R.string.project_name_empty_error));
            return;
        }

        Project project = new Project(name, description);

        int id = db.addProject(project);
        db.linkProjectToUser(id, currentUser, true);

        finish();
    }
}
