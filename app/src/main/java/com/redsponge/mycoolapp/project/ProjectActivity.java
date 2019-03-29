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
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.project.category.Category;
import com.redsponge.mycoolapp.project.invite.Invite;
import com.redsponge.mycoolapp.user.User;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.AlertUtils;
import com.redsponge.mycoolapp.utils.Constants;
import com.redsponge.mycoolapp.utils.ImageUtils;

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
        this.project = (Project) getIntent().getExtras().get("project");

        setupDisplay();
    }

    private void setupDisplay() {
        this.title = (TextView) findViewById(R.id.projectTitle);
        this.description = (TextView) findViewById(R.id.projectDescription);
        this.inviteUserInput = (AutoCompleteTextView) findViewById(R.id.inviteNameInput);
        this.imgView = (ImageView) findViewById(R.id.projectIcon);
        this.deleteButton = (Button) findViewById(R.id.deleteProject);
        this.title.setText(project.name);
        this.description.setText(project.description);

        String img = db.getIcon(project.id);
        if(img != null) {
            this.imgView.setImageBitmap(ImageUtils.decode(img));
        }

        if(db.isUserAdmin(currentUser, project.id)) {
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
            deleteButton.setText("Leave Project");
        }

        ArrayAdapter<User> names = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, db.getUnInvitedUsers(project.id));
        inviteUserInput.setAdapter(names);
    }

    private void leaveProject() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    db.unlinkProjectFromUser(project.id, currentUser);
                    finish();
                }
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("You cannot rejoin the project unless you're re-invited! Are you sure?")
                .setPositiveButton("Yes", listener)
                .setNegativeButton("No", listener)
                .show();
    }

    /**
     * Deletes a project (asks if sure first)
     */
    public void deleteProject() {
        if(db.isUserAdmin(currentUser, project.id)) {

            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        db.deleteProject(project.id);
                        finish();
                    }
                }
            };

            new AlertDialog.Builder(this)
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
            AlertUtils.showAlert(this, "Success", query.name + " successfully invited!", null);
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

            db.setProjectIcon(project.id, ImageUtils.encode(scaled));
            imgView.setImageBitmap(scaled);

        }
    }

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

    public void changeImageButtonClicked(View view) {
        if(Build.VERSION.SDK_INT >= 23) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                return;
            }
        }
        changeImageShowGallery();
    }

    public void changeImageShowGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, IMAGE_PICK_RESULT);
    }

    public void editCategory(View view) {
        final Spinner spinner = new Spinner(this);
        final ArrayAdapter<Category> options = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(options);

        options.add(new Category(Constants.CATEGORY_ALL_ID, "None", currentUser, db));
        options.addAll(db.getCategories(currentUser));

        for(int i = 1; i < options.getCount(); i++) { // Skip category None
            Category c = options.getItem(i);
            if(db.isProjectInCategory(project.id, c.id)) {
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
                        if(selected.id == Constants.CATEGORY_ALL_ID) {
                            db.unlinkProjectFromUserCategories(project.id, currentUser);
                        } else {
                            db.linkProjectToCategory(((Category) spinner.getSelectedItem()).id, project.id);
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

    public void changeName(View view) {
        final EditText input = new EditText(this);
        input.setHint("New name");

        new AlertDialog.Builder(this)
                .setTitle("Change name")
                .setView(input)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!input.getText().toString().isEmpty()) {
                            db.setProjectName(project.id, input.getText().toString());
                            title.setText(input.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
