package com.redsponge.mycoolapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.redsponge.mycoolapp.project.ProjectViewActivity;
import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.user.User;
import com.redsponge.mycoolapp.utils.AbstractActivity;

public class LoginActivity extends AbstractActivity {

    private EditText username;
    private EditText password;
    private CheckBox keepLoggedIn;

    @Override
    protected void initialize() {
        currentUser = LoginUtils.getCurrentUser(this);

        if(currentUser != -1) {
            switchToActivity(ProjectViewActivity.class, true);
            return;
        }

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        keepLoggedIn = (CheckBox) findViewById(R.id.keepLoggedIn);
    }

    /**
     * Tries to login into an account. if successful transfers the user to the {@link ProjectViewActivity} activity
     * @param view
     */
    public void tryLogin(View view) {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if(username.isEmpty() || password.isEmpty()) {
            return;
        }

        int hashedPw = LoginUtils.hashPw(password);
        User user = db.getUser(username);

        if(user != null && user.password == hashedPw) {
            if(keepLoggedIn.isChecked()) {
                LoginUtils.registerCurrentUser(this,user.id);
            }
            currentUser = user.id;
            switchToActivity(ProjectViewActivity.class, true);
        } else {
            this.username.setError("Unknown username or password!");
            this.password.setError("Unknown username or password!");
        }
    }

    /**
     * Transfers the user to the {@link RegisterActivity}
     * @param view
     */
    public void enterRegister(View view) {
        switchToActivity(RegisterActivity.class, false);
    }
}
