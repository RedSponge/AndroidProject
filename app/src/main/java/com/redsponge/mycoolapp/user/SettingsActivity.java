package com.redsponge.mycoolapp.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.login.LoginUtils;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.AlertUtils;

/**
 * The activity in which username and password can be changed.
 */
public class SettingsActivity extends AbstractActivity {

    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_settings);

        usernameInput = (EditText) findViewById(R.id.usernameChangeInput);
        passwordInput = (EditText) findViewById(R.id.passwordChangeInput);
    }

    public void changeUsername(View view) {
        if(db.getUser(usernameInput.getText().toString()) != null) {
            usernameInput.setError(getString(R.string.username_taken_error));
            return;
        } else if(!LoginUtils.isUsernameValid(usernameInput.getText().toString())) {
            usernameInput.setError(getString(R.string.invalid_username_error));
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
        if(!LoginUtils.isPasswordValid(passwordInput.getText().toString())) {
            this.passwordInput.setError(getString(R.string.invalid_password_error));
            return;
        }
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
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        input.requestFocus();

        new AlertDialog.Builder(this)
                .setTitle("Enter current password:").
                setView(input).
                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String pw = input.getText().toString();
                if(db.getUser(currentUser).password == LoginUtils.hashPw(pw)) {
                    ifAssured.run();
                } else {
                    AlertUtils.showAlert(SettingsActivity.this, "Error", "Incorrect password!", null);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
                .show();
    }
}
