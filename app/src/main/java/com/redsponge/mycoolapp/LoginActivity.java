package com.redsponge.mycoolapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.db.DatabaseHandler;

public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;

    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwordInput);
        dbHandler = new DatabaseHandler(this);
        dbHandler.restart();
        dbHandler.addUser(new User("EranG", "yesyes"));
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
            new AlertDialog.Builder(this)
                    .setTitle("Yay")
                    .show();
        }
    }
}
