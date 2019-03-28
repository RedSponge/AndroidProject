package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.login.LoginActivity;
import com.redsponge.mycoolapp.login.LoginUtils;
import com.redsponge.mycoolapp.project.category.Category;
import com.redsponge.mycoolapp.project.invite.InviteViewActivity;
import com.redsponge.mycoolapp.utils.Constants;
import com.redsponge.mycoolapp.utils.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class ProjectViewActivity extends Activity {

    private ProjectsAdapter listAdapter;
    private DatabaseHandler db;
    private int currentUser;

    private Spinner categorySelector;
    private ArrayAdapter<Category> spinnerAdapter;

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

        spinnerAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item);
        categorySelector = (Spinner) findViewById(R.id.categorySelector);
        categorySelector.setAdapter(spinnerAdapter);

        queryCategories();
        queryProjects();
        updateInvitesButton();

        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                findViewById(R.id.deleteCategoryButton).setEnabled(spinnerAdapter.getItem(position).id != Constants.CATEGORY_ALL_ID);
                queryProjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void queryCategories() {
        spinnerAdapter.clear();
        spinnerAdapter.add(new Category(Constants.CATEGORY_ALL_ID, "All", currentUser, db));
        for(Category c : db.getCategories(currentUser)) {
            spinnerAdapter.add(c);
        }
    }

    /**
     * Updates the {@link ProjectViewActivity#listAdapter} based on the projects from the database
     */
    private void queryProjects() {
        listAdapter.clear();
        Category c = (Category) categorySelector.getSelectedItem();

        List<Project> projects;
        if(c == null) {
            projects = db.getAllProjects(currentUser);
        } else {
            projects = db.getProjectsForCategory(currentUser, c.id);
        }

        for (Project project : projects) {
            listAdapter.add(project);
        }

        String welcome = String.format(getString(R.string.placeholder_welcome), db.getUser(currentUser).name);
        ((TextView) findViewById(R.id.welcome_message)).setText(welcome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryCategories();
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
        Intent i = new Intent(this, InviteViewActivity.class);
        i.putExtra("currentUser", currentUser);
        startActivity(i);
    }

    public void addCategory(final View view) {

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.requestFocus();

        AlertDialog b = new AlertDialog.Builder(this)
                .setTitle("New category")
                .setView(input)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Check if name is avaliable, then add or show error
                        String name = input.getText().toString();
                        if(true) {
                            db.addCategory(new Category(name, currentUser, db));
                            queryCategories();
                        } else {
                            input.setError("Name taken or empty!");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

        final Button okButton = b.getButton(DialogInterface.BUTTON_POSITIVE);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(db.getCategory(currentUser, s.toString()) != null) {
                    input.setError("Name taken!");
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
        });


    }

    public void deleteCategory(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Deleting a category is permanent! all projects in the category will become categoryless")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteCategory(((Category) categorySelector.getSelectedItem()).id);
                        queryCategories();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}