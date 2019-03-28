package com.redsponge.mycoolapp.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.project.category.Category;
import com.redsponge.mycoolapp.project.invite.Invite;
import com.redsponge.mycoolapp.utils.AlertUtils;
import com.redsponge.mycoolapp.utils.Constants;
import com.redsponge.mycoolapp.utils.ImageUtils;
import com.redsponge.mycoolapp.utils.User;

public class ProjectActivity extends Activity {

    private static final int IMAGE_PICK_RESULT = 1;

    private Project project;
    private TextView title;
    private TextView description;

    private EditText inviteUserInput;

    private DatabaseHandler db;
    private int currentUser;

    private ImageView imgView;
    private Button deleteButton;

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
        this.imgView = (ImageView) findViewById(R.id.projectIcon);
        this.deleteButton = (Button) findViewById(R.id.deleteProject);
        this.title.setText(project.name);
        this.description.setText(project.description);

        String img = db.getIcon(project.id);
        if(img != null) {
            this.imgView.setImageBitmap(ImageUtils.decode(img));
        }

        this.deleteButton.setEnabled(db.isUserAdmin(currentUser, project.id));
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
                        finish();
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
            imgView.setImageBitmap(bmp);

            db.setProjectIcon(project.id, ImageUtils.encode(bmp));
        }
    }

    public void changeImage(View view) {
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
}
