package com.redsponge.mycoolapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.redsponge.mycoolapp.project.ProjectViewActivity;
import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.user.User;

public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;
    private CheckBox keepLoggedIn;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentUser = LoginUtils.getCurrentUser(this);

        if(currentUser != -1) {
            Intent intent = new Intent(this, ProjectViewActivity.class);
            intent.putExtra("currentUser", currentUser);
            finish();
            startActivity(intent);
        }

        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);
        keepLoggedIn = (CheckBox) findViewById(R.id.keepLoggedIn);
        dbHandler = new DatabaseHandler(this);
    }

    public void tryLogin(View view) {
        String username = this.username.getText().toString();
        String password = this.password.getText().toString();

        if(username.isEmpty() || password.isEmpty()) {
            return;
        }

        int hashedPw = LoginUtils.hashPw(password);
        User user = dbHandler.getUser(username);

        if(user != null && user.password == hashedPw) {
            Intent intent = new Intent(this, ProjectViewActivity.class);
            intent.putExtra("currentUser", user.id);

            if(keepLoggedIn.isChecked()) {
                LoginUtils.registerCurrentUser(this,user.id);
            }

            finish();
            startActivity(intent);
        } else {
            this.username.setError("Unknown username or password!");
            this.password.setError("Unknown username or password!");
        }
    }

    public void enterRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
