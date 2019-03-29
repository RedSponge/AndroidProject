package com.redsponge.mycoolapp;

import android.app.Application;

import com.redsponge.mycoolapp.db.DatabaseHandler;

public class ApplicationLauncher extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHandler.initializeDatabase(this);
    }
}
