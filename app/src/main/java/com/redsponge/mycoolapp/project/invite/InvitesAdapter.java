package com.redsponge.mycoolapp.project.invite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.utils.ImageUtils;

public class InvitesAdapter extends ArrayAdapter<Invite> {

    private DatabaseHandler db;

    public InvitesAdapter(Context context) {
        super(context, 0);
        db = new DatabaseHandler(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Invite invite = getItem(position);

        if(invite == null) {
            throw new RuntimeException("Invite cannot be null!");
        }

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_invite, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.projectName);
        TextView from = (TextView) convertView.findViewById(R.id.projectFrom);
        Button accept = (Button) convertView.findViewById(R.id.acceptButton);
        Button decline = (Button) convertView.findViewById(R.id.declineButton);
        ImageView image = (ImageView) convertView.findViewById(R.id.projectIcon);
        name.setText(db.getProject(invite.projectId).name);
        from.setText(String.format(getContext().getString(R.string.placeholder_invite_from), db.getUser(invite.idFrom).name));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.linkProjectToUser(invite.projectId, invite.idTo, invite.shouldBeAdmin == 1);
                db.removeInvite(invite);
                remove(invite);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.removeInvite(invite);
                remove(invite);
            }
        });

        String icon = db.getIcon(invite.projectId);
        if(icon != null) {
            image.setImageBitmap(ImageUtils.decode(icon));
        }

        return convertView;
    }
}