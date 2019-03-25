package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;

public class ProjectActivity extends Activity {

    private Project project;
    private TextView title;
    private TextView description;
    private DatabaseHandler dbHandler;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        this.project = (Project) getIntent().getExtras().get("project");
        this.currentUser = getIntent().getExtras().getInt("currentUser");

        dbHandler = new DatabaseHandler(this);
        setupDisplay();
    }

    private void setupDisplay() {
        this.title = (TextView) findViewById(R.id.projectTitle);
        this.description = (TextView) findViewById(R.id.projectDescription);

        this.title.setText(project.name);
        this.description.setText(project.description);
    }

    public void deleteProject(View view) {
        if(dbHandler.isUserAdmin(currentUser, project.id)) {

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        dbHandler.deleteProject(project.id);
                        processDelete(project);
                    }
                }
            };

            AlertDialog surePrompt = new AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("Deleting a project is permanent! Are you sure?")
                    .setPositiveButton("Yes", listener)
                    .setNegativeButton("No", listener)
                    .show();
        } else {
            AlertDialog al = new AlertDialog.Builder(this)
                    .setTitle("Whoops!")
                    .setMessage("You do not have permission to do this!")
                    .setPositiveButton("OK", (new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }))
                    .show();
        }
    }

    private void processDelete(Project proj) {

    }
}
