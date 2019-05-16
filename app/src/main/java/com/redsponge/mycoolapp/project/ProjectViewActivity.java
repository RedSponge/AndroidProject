package com.redsponge.mycoolapp.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.redsponge.mycoolapp.login.LoginActivity;
import com.redsponge.mycoolapp.login.LoginUtils;
import com.redsponge.mycoolapp.project.category.Category;
import com.redsponge.mycoolapp.project.invite.InviteViewActivity;
import com.redsponge.mycoolapp.user.SettingsActivity;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The activity which shows all projects of the user
 */
public class ProjectViewActivity extends AbstractActivity {

    private ProjectsAdapter projectsAdapter;

    private Spinner categorySelector;
    private ArrayAdapter<Category> categoryAdapter;

    private ListView projectViewList;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_project_view);


        // If user has been deleted
        if(db.getUser(currentUser) == null) {
            Log.i(getClass().getName(), "Current User Doesn't Exist! " + currentUser);
            logout(null);
            return;
        }

        projectsAdapter = new ProjectsAdapter(this, new ArrayList<Project>(), db);

        projectViewList = (ListView) findViewById(R.id.projectViewList);
        projectViewList.setAdapter(projectsAdapter);

        categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item);

        categorySelector = (Spinner) findViewById(R.id.categorySelector);
        categorySelector.setAdapter(categoryAdapter);

        queryCategories();
        queryProjects();
        updateInvitesButton();

        // Add category change listener
        categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                findViewById(R.id.deleteCategoryButton)
                        .setEnabled(Objects.requireNonNull(categoryAdapter.getItem(position)).getId() != Constants.CATEGORY_ALL_ID);
                queryProjects();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Updates the {@link ProjectViewActivity#categoryAdapter} based on the categories from the database
     */
    private void queryCategories() {
        categoryAdapter.clear();

        categoryAdapter.add(new Category(Constants.CATEGORY_ALL_ID, "All", currentUser));

        for(Category c : db.getCategories(currentUser)) {
            categoryAdapter.add(c);
        }
    }

    /**
     * Updates the {@link ProjectViewActivity#projectsAdapter} based on the projects from the database
     */
    private void queryProjects() {
        projectsAdapter.clear();
        final Category c = (Category) categorySelector.getSelectedItem();

        final List<Project> projects;
        if(c == null) {
            projects = db.getAllProjects(currentUser);
        } else {
            projects = db.getProjectsForCategory(currentUser, c.getId());
        }

        projectsAdapter.addAll(projects);

        String welcome = String.format(getString(R.string.placeholder_welcome), db.getUser(currentUser).getName());
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
        switchToActivity(SettingsActivity.class, false);
    }

    /**
     * Enters the new project screen ({@link NewProjectActivity})
     */
    public void newProject(View view) {
        switchToActivity(NewProjectActivity.class, false);
    }

    /**
     * Logs out from a user, removing it from the {@link android.content.SharedPreferences}
     */
    public void logout(View view) {
        LoginUtils.clearCurrentUser(this);
        switchToActivity(LoginActivity.class, true);
    }

    /**
     * Called when the view invites button is pressed
     */
    public void viewInvites(View view) {
        switchToActivity(InviteViewActivity.class, false);
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
                        String name = input.getText().toString();
                        db.addCategory(new Category(name, currentUser));
                        queryCategories();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();

        final Button okButton = b.getButton(DialogInterface.BUTTON_POSITIVE);

        // Disable ok button when category name which already exists is entered
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(db.getCategoryByName(currentUser, s.toString()) != null) {
                    input.setError(getString(R.string.category_name_taken_error));
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
            // region Ignored
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            // endregion
        });


    }

    /**
     * Deletes a category if the user confirms it
     */
    public void deleteCategory(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Deleting a category is permanent! all projects in the category will become categoryless")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteCategory(((Category) categorySelector.getSelectedItem()).getId());
                        queryCategories();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}