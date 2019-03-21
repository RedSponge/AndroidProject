package com.redsponge.mycoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ProjectActivity extends Activity {

    private Project project;
    private TextView title;
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        this.project = (Project) getIntent().getExtras().get(Const.EXTRA_PROJECT);
        setupDisplay();
    }

    private void setupDisplay() {
        this.title = findViewById(R.id.projectTitle);
        this.description = findViewById(R.id.projectDescription);

        this.title.setText(project.name);
        this.description.setText(project.description);
    }
}
