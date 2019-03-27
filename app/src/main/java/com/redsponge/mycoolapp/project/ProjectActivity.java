package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.utils.User;

public class ProjectActivity extends Activity {

    private Project project;
    private TextView title;
    private TextView description;

    private EditText inviteUserInput;

    private DatabaseHandler db;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        this.project = (Project) getIntent().getExtras().get("project");
        this.currentUser = getIntent().getExtras().getInt("currentUser");

        db = new DatabaseHandler(this);
        setupDisplay();
    }

    private void setupDisplay() {
        this.title = (TextView) findViewById(R.id.projectTitle);
        this.description = (TextView) findViewById(R.id.projectDescription);
        this.inviteUserInput = (EditText) findViewById(R.id.inviteNameInput);

        this.title.setText(project.name);
        this.description.setText(project.description);
    }

    /**
     * Deletes a project (asks if sure first)
     * @param view
     */
    public void deleteProject(View view) {
        if(db.isUserAdmin(currentUser, project.id)) {

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        db.deleteProject(project.id);
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

    /**
     * Shows a popup to edit the description of a project
     */
    public void editDescription(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("New Description");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.requestFocus();

        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String desc = input.getText().toString();
                db.updateProjectDescription(project.id, desc);
                project.description = desc;
                setupDisplay();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Tries to invite a user and displays the appropriate error message if cannot
     */
    public void tryInviteUser(View view) {
        String name = inviteUserInput.getText().toString();
        User query = db.getUser(name);

        boolean userExists = query != null;
        boolean isSelf = false;
        boolean isInvited = false;
        boolean isPartOfProject = false;

        if (query != null) {
            isSelf = query.id == currentUser;
            isInvited = db.isInvited(query.id, project.id);
            isPartOfProject = db.isPartOfProject(query.id, project.id);
        }

        if(userExists && !isSelf && !isInvited && !isPartOfProject) {
            Invite invite = new Invite(currentUser, query.id, project.id, false);
            db.addInvite(invite);
            inviteUserInput.setError(null);
        } else {
            if(!userExists) {
                inviteUserInput.setError("Couldn't find user!");
            } else if(isSelf) {
                inviteUserInput.setError("You can't invite yourself!");
            } else if(isInvited){
                inviteUserInput.setError("This person is already invited!");
            } else /*if (isPartOfProject)*/ {
                inviteUserInput.setError("This person is already a part of this project!");
            }
        }
    }
}
