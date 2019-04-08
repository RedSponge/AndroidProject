package com.redsponge.mycoolapp.login;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.user.User;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.alert.AlertUtils;
import com.redsponge.mycoolapp.R;

public class RegisterActivity extends AbstractActivity {

    private EditText usernameInput, passwordInput, passwordConfirmInput;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_register);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordConfirmInput = (EditText) findViewById(R.id.passwordConfirmInput);
    }


    public void registerUser(View view) {
        String username = this.usernameInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        String passwordConfirm = this.passwordConfirmInput.getText().toString();

        if(username.isEmpty()) {
            usernameInput.setError(getString(R.string.username_empty_error));
        }
        else if(!LoginUtils.isUsernameValid(username)) {
            usernameInput.setError(getString(R.string.invalid_username_error));
        }
        else if(password.isEmpty()) {
            passwordInput.setError(getString(R.string.password_empty_error));
        }
        else if(!LoginUtils.isPasswordValid(password)) {
            passwordInput.setError(getString(R.string.invalid_password_error));
        }
        else if(!passwordConfirm.equals(password)) {
            passwordConfirmInput.setError(getString(R.string.unmatching_passwords_error));
        }
        else if(db.getUser(username) != null) {
            usernameInput.setError(getString(R.string.username_taken_error));
        }
        else {
            createUser(username, password);
        }
    }

    private void createUser(String username, String password) {
        AlertUtils.showAlert(this, "Success!", "Account created! username: " + username + ", password: " + password, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        User user = new User(username, password);
        db.addUser(user);
    }
}
