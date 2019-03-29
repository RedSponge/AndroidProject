package com.redsponge.mycoolapp.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.user.User;
import com.redsponge.mycoolapp.utils.AlertUtils;
import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;

public class RegisterActivity extends Activity {

    private DatabaseHandler db;
    private EditText username, password, passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHandler(this);

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirmInput);
    }


    public void registerUser(View view) {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();
        String passwordConfirm = this.passwordConfirm.getText().toString();

        if(username.isEmpty()) {
            this.username.setError(getString(R.string.username_empty_error));
        } else if(!LoginUtils.isUsernameValid(username)) {
            this.username.setError(getString(R.string.invalid_username_error));
        } else if(password.isEmpty()) {
            this.password.setError(getString(R.string.password_empty_error));
        } else if(!LoginUtils.isPasswordValid(password)) {
            this.password.setError(getString(R.string.invalid_password_error));
        } else if(!passwordConfirm.equals(password)) {
            this.passwordConfirm.setError(getString(R.string.unmatching_passwords_error));
        } else if(db.getUser(username) != null) {
            this.username.setError(getString(R.string.username_taken_error));
        } else {
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
