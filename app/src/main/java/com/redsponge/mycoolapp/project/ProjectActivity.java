package com.redsponge.mycoolapp.project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.project.category.Category;
import com.redsponge.mycoolapp.project.event.EventsActivity;
import com.redsponge.mycoolapp.project.invite.Invite;
import com.redsponge.mycoolapp.user.User;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.alert.AlertUtils;
import com.redsponge.mycoolapp.utils.Constants;
import com.redsponge.mycoolapp.utils.ImageUtils;
import com.redsponge.mycoolapp.utils.alert.OnTextAcceptListener;

/**
 * An activity which displays a single project, in which the user can configure that project.
 */
public class ProjectActivity extends AbstractActivity {

    private static final int IMAGE_PICK_RESULT = 1;

    private Project project;
    private TextView title;
    private TextView description;

    private AutoCompleteTextView inviteUserInput;

    private ImageView imgView;
    private Button deleteButton;

    private final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_project);
        this.project = (Project) getIntent().getExtras().get(Constants.EXTRA_PROJECT_OBJ);

        this.title = (TextView) findViewById(R.id.projectTitle);
        this.description = (TextView) findViewById(R.id.projectDescription);
        this.inviteUserInput = (AutoCompleteTextView) findViewById(R.id.inviteNameInput);
        this.imgView = (ImageView) findViewById(R.id.projectIcon);
        this.deleteButton = (Button) findViewById(R.id.deleteProject);

        setupDisplay();
    }


    private void setupDisplay() {
        this.title.setText(project.getName());
        this.description.setText(project.getDescription());

        String img = db.getIcon(project.getId());
        if(img != null) {
            this.imgView.setImageBitmap(ImageUtils.decode(img));
        }

        if(db.isUserAdmin(currentUser, project.getId())) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteProject();
                }
            });
        } else {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leaveProject();
                }
            });
            deleteButton.setText(R.string.leave_project_text);
        }

        ArrayAdapter<User> names = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, db.getUninvitedUsers(project.getId()));
        inviteUserInput.setAdapter(names);
    }

    /**
     * Leaves a project, this will be called if a user isn't an admin on the project (asks if sure first)
     */
    private void leaveProject() {
        AlertUtils.showConfirmPrompt(this, "Warning", "You cannot rejoin the project unless you're re-invited! Are you sure?",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.unlinkProjectFromUser(project.getId(), currentUser);
                finish();
            }
        });
    }

    /**
     * Deletes a project (asks if sure first)
     */
    public void deleteProject() {
        if(db.isUserAdmin(currentUser, project.getId())) {
            AlertUtils.showConfirmPrompt(this, "Warning", "Deleting a project is permanent! Are you sure?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which == DialogInterface.BUTTON_POSITIVE) {
                                db.deleteProject(project.getId());
                                finish();
                            }
                        }
                    });
        } else {
            AlertUtils.showAlert(this, "Whoops", "You do not have permission to do this!", null);
        }
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
            isSelf = query.getId() == currentUser;
            isInvited = db.isInvited(query.getId(), project.getId());
            isPartOfProject = db.isPartOfProject(query.getId(), project.getId());
        }

        if(userExists && !isSelf && !isInvited && !isPartOfProject) {
            Invite invite = new Invite(currentUser, query.getId(), project.getId(), false);
            db.addInvite(invite);
            AlertUtils.showAlert(this, "Success", query.getName() + " successfully invited!", null);
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

    /**
     * Called when the user has granted (or not) permission to access files
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    changeImageShowGallery();
                } else {
                    AlertUtils.showAlert(this, "Whoops", "To change image, this permission must be granted!", null);
                }
        }
    }

    /**
     * Checks for permission and if they're granted proceeds to require image from gallery
     */
    public void changeImageButtonClicked(View view) {
        if(Build.VERSION.SDK_INT >= 23) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                return;
            }
        }
        changeImageShowGallery();
    }

    /**
     * Opens the gallery for image choosing
     */
    public void changeImageShowGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, IMAGE_PICK_RESULT);
    }

    /**
     * Called once the user has chosen an image from the gallery (in this case)
     * Decodes the image, scales it, displays it, and saves it to the database
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) return;
        if(requestCode == IMAGE_PICK_RESULT) {
            Uri selectedImage = data.getData();

            String[] filePathCol = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathCol, null, null, null);

            c.moveToFirst();
            int colIndex = c.getColumnIndex(filePathCol[0]);

            String imgDecodableString = c.getString(colIndex);
            c.close();


            Bitmap bmp = BitmapFactory.decodeFile(imgDecodableString);
            Bitmap scaled = ImageUtils.scaleDown(bmp);

            bmp.recycle();

            db.setProjectIcon(project.getId(), ImageUtils.encode(scaled));
            imgView.setImageBitmap(scaled);

        }
    }

    /**
     * Edits the category of the project
     * Shows an alert with a category selection spinner
     */
    public void editCategory(View view) {
        final Spinner spinner = new Spinner(this);
        final ArrayAdapter<Category> options = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(options);

        options.add(new Category(Constants.CATEGORY_ALL_ID, "None", currentUser));
        options.addAll(db.getCategories(currentUser));

        for(int i = 1; i < options.getCount(); i++) { // Skip category None
            Category c = options.getItem(i);
            if(db.isProjectInCategory(project.getId(), c.getId())) {
                spinner.setSelection(i);
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Change Category")
                .setView(spinner)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Category selected = (Category) spinner.getSelectedItem();
                        if(selected.getId() == Constants.CATEGORY_ALL_ID) {
                            db.unlinkProjectFromUserCategories(project.getId(), currentUser);
                        } else {
                            db.linkProjectToCategory(((Category) spinner.getSelectedItem()).getId(), project.getId());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    /**
     * Shows a popup to edit the description of a project
     */
    public void editDescription(View view) {
        AlertUtils.showTextPrompt(this, "New Description", new OnTextAcceptListener() {
            @Override
            public void onTextEntered(DialogInterface dialog, String input) {
                db.updateProjectDescription(project.getId(), input);
                project.setDescription(input);
                setupDisplay();
            }
        }, null, project.getDescription(), false, "New Description");
    }

    /**
     * Changes the name of the project
     */
    public void changeName(View view) {
        AlertUtils.showTextPrompt(this, "Change name", new OnTextAcceptListener() {
            @Override
            public void onTextEntered(DialogInterface dialog, String input) {
                db.setProjectName(project.getId(), input);
                title.setText(input);
                project.setName(input);
            }
        }, null, project.getName(), false, "New Name");
    }

    public void enterManageEvents(View view) {
        switchToActivity(EventsActivity.class, false, Constants.EXTRA_PROJECT_OBJ, project);
    }
}
