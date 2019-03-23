package com.redsponge.mycoolapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.redsponge.mycoolapp.db.DatabaseHandler;

public class SettingsActivity extends Activity{

    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        databaseHandler = new DatabaseHandler(this);
    }

    public void wipeDatabase(View view) {
        databaseHandler.restart();
    }
}
