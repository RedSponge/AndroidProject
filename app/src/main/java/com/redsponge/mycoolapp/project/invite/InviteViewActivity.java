package com.redsponge.mycoolapp.project.invite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;

public class InviteViewActivity extends Activity{

    private ListView invites;
    private InvitesAdapter adapter;
    private DatabaseHandler db;
    private int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invites);

        db = new DatabaseHandler(this);
        currentUser = getIntent().getIntExtra("currentUser", -1);

        if(currentUser == -1) {
            throw new RuntimeException("CurrentUser cannot be null!");
        }

        invites = (ListView) findViewById(R.id.invitesViewList);
        adapter = new InvitesAdapter(this);

        invites.setAdapter(adapter);

        queryInvites();
    }

    private void queryInvites() {
        adapter.clear();
        adapter.addAll(db.getInvites(currentUser));
    }
}
