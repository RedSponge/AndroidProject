package com.redsponge.mycoolapp.user;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.login.LoginUtils;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.alert.AlertUtils;
import com.redsponge.mycoolapp.utils.alert.OnTextAcceptListener;

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

    /**
     * Run when the change username button is clicked, assures the username is valid and then changes it after
     * validation of the user
     */
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
                Toast.makeText(SettingsActivity.this, "Changed your username successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Run when the change password button is clicked, assures the password is valid and then changes it after
     * validation of the user
     */
    public void changePassword(View view) {
        if(!LoginUtils.isPasswordValid(passwordInput.getText().toString())) {
            this.passwordInput.setError(getString(R.string.invalid_password_error));
            return;
        }
        assureUser(new Runnable() {
            @Override
            public void run() {
                db.updatePassword(currentUser, LoginUtils.hashPw(passwordInput.getText().toString()));
                Toast.makeText(SettingsActivity.this, "Changed your password successfully!", Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Checks if the user knows their password before executing code
     * @param ifAssured The code to execute if the user has passed the check
     */
    private void assureUser(final Runnable ifAssured) {
        AlertUtils.showTextPrompt(this, "Enter current password: ", new OnTextAcceptListener() {
            @Override
            public void onTextEntered(DialogInterface dialog, String input) {
                if(db.getUser(currentUser).getPassword() == LoginUtils.hashPw(input)) {
                    ifAssured.run();
                } else {
                    AlertUtils.showAlert(SettingsActivity.this, "Error", "Incorrect password!", null);
                }
            }
        }, null, null, true, "Enter Password");
    }
}
