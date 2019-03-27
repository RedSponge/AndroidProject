package com.redsponge.mycoolapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;

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
        if(assureUser()) {

        }
    }

    private boolean assureUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enter current password:");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.requestFocus();

        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String pw = input.getText().toString();
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

    public void changePassword(View view) {

    }
}
