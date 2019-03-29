package com.redsponge.mycoolapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.redsponge.mycoolapp.db.DatabaseHandler;

import java.util.Objects;

/**
 * A base class for all activities of this program
 */
public abstract class AbstractActivity extends Activity {

    protected DatabaseHandler db;
    protected int currentUser;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHandler.getInstance();

        if(getIntent().getExtras() != null) {
            currentUser = getIntent().getExtras().getInt("currentUser", -1);
        } else {
            currentUser = -1;
        }

        initialize();
    }

    /**
     * The initializing method of the activity, replaces {@link Activity#onCreate(Bundle)}
     */
    protected abstract void initialize();

    /**
     * Switches to a new activity of this program
     * @param newActivity The new activity's class
     * @param finish Should this activity be closed after switching
     */
    protected void switchToActivity(Class<? extends AbstractActivity> newActivity, boolean finish) {
        Intent intent = new Intent(this, newActivity);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);

        if(finish) {
            finish();
        }
    }
}
