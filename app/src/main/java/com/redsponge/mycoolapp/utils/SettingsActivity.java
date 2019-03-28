package com.redsponge.mycoolapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.login.LoginUtils;

public class SettingsActivity extends Activity{

    private DatabaseHandler db;
    private EditText usernameInput;
    private EditText passwordInput;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DatabaseHandler(this);

        usernameInput = (EditText) findViewById(R.id.usernameChangeInput);
        passwordInput = (EditText) findViewById(R.id.passwordChangeInput);

        currentUser = getIntent().getIntExtra("currentUser", -1);
        if(currentUser == -1) {
            throw new RuntimeException("this shouldn't happen!");
        }
    }

    public void changeUsername(View view) {
        if(db.getUser(usernameInput.getText().toString()) != null) {
            usernameInput.setError("Username already taken!");
            return;
        }
        assureUser(new Runnable() {
            @Override
            public void run() {
                db.updateUserName(currentUser, usernameInput.getText().toString());
                AlertUtils.showAlert(SettingsActivity.this, "Success!", "Changed your username to " + usernameInput.getText().toString(), null);
            }
        });
    }

    public void changePassword(View view) {
        assureUser(new Runnable() {
            @Override
            public void run() {
                db.updatePassword(currentUser, LoginUtils.hashPw(passwordInput.getText().toString()));
                AlertUtils.showAlert(SettingsActivity.this, "Success!", "Changed your password to " + passwordInput.getText().toString(), null);
            }
        });
    }


    /**
     * Checks if the user knows their password before executing code
     * @param ifAssured The code to execute if the user has passed the check
     */
    private void assureUser(final Runnable ifAssured) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enter current password:");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        input.requestFocus();

        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String pw = input.getText().toString();
                if(db.getUser(currentUser).password == LoginUtils.hashPw(pw)) {
                    ifAssured.run();
                } else {
                    AlertUtils.showAlert(SettingsActivity.this, "Error", "Incorrect password!", null);
                }
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
}
