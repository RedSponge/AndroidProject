package com.redsponge.mycoolapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.utils.AlertUtils;
import com.redsponge.mycoolapp.project.ProjectViewActivity;
import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.utils.User;
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
            this.username.setError("Username cannot be empty!");
        } else if(password.isEmpty()) {
            this.password.setError("Password cannot be empty!");
        } else if(!passwordConfirm.equals(password)) {
            this.passwordConfirm.setError("Passwords don't match!");
        } else if(db.getUser(username) != null) {
            this.username.setError("Username taken!");
        } else {
            createUser(username, password);
        }
    }

    private void createUser(String username, String password) {
        AlertUtils.showAlert(this, "Success!", "Account created: " + username + "," + password);
        User user = new User(username, password);
        int id = db.addUser(user);
        finish();

        Intent intent = new Intent(this, ProjectViewActivity.class);
        intent.putExtra("currentUser", id);
        LoginUtils.registerCurrentUser(this, id);
        startActivity(intent);
    }
}
