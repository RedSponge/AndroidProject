package com.redsponge.mycoolapp.project.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.redsponge.mycoolapp.R;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        Event event = getItem(position);
        TextView name = (TextView) convertView.findViewById(R.id.event_name);
        TextView status = (TextView) convertView.findViewById(R.id.event_status);
        TextView deadline = (TextView) convertView.findViewById(R.id.event_deadline);

        Button delete = (Button) convertView.findViewById(R.id.event_delete_button);
        Button changeStatus = (Button) convertView.findViewById(R.id.event_change_status_button);

        name.setText(event.getName());
        status.setText("" + event.getStatus());
        deadline.setText("" + event.getDeadline());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Deleting Project", Toast.LENGTH_LONG).show();
            }
        });

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Changing Status For Project", Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }
}
