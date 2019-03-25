package com.redsponge.mycoolapp.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.project.ProjectViewActivity;
import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.utils.User;
import com.redsponge.mycoolapp.db.DatabaseHandler;

public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;

    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);

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

        Log.i("", "" + hashedPw + " " + user);
        if(user != null && user.getPassword() == hashedPw) {
            Intent intent = new Intent(this, ProjectViewActivity.class);
            intent.putExtra("currentUser", user.id);

            finish();
            startActivity(intent);
        }
    }

    public void enterRegister(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
