package com.redsponge.mycoolapp.project.invite;

import android.widget.ListView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.utils.AbstractActivity;

public class InviteViewActivity extends AbstractActivity {

    private ListView invites;
    private InvitesAdapter adapter;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_invites);

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
