package com.redsponge.mycoolapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.redsponge.mycoolapp.db.DatabaseHandler;

import java.io.Serializable;
import java.util.Objects;

/**
 * A base class for all activities of this app
 */
public abstract class AbstractActivity extends Activity {

    protected DatabaseHandler db;
    protected int currentUser;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHandler.getInstance();

        if(getIntent().getExtras() != null) {
            currentUser = getIntent().getExtras().getInt(Constants.EXTRA_USER_ID, -1);
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
    public void switchToActivity(Class<? extends AbstractActivity> newActivity, boolean finish, Object... extras) {
        Intent intent = new Intent(this, newActivity);
        intent.putExtra(Constants.EXTRA_USER_ID, currentUser);

        for(int i = 0; i < extras.length / 2; i++) {
            intent.putExtra((String) extras[i * 2], (Serializable) extras[i * 2 + 1]);
        }

        startActivity(intent);

        if(finish) {
            finish();
        }
    }
}
