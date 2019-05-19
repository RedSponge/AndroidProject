package com.redsponge.mycoolapp.project.event;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.utils.views.DateTextView;
import com.redsponge.mycoolapp.utils.views.EditableTextView;
import com.redsponge.mycoolapp.utils.alert.AlertUtils;
import com.redsponge.mycoolapp.utils.alert.OnTextAcceptListener;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }

        final Event event = getItem(position);
        EditableTextView name = (EditableTextView) convertView.findViewById(R.id.event_name);
        TextView status = (TextView) convertView.findViewById(R.id.event_status);
        DateTextView deadline = (DateTextView) convertView.findViewById(R.id.event_deadline);

        Button delete = (Button) convertView.findViewById(R.id.event_delete_button);
        Button changeStatus = (Button) convertView.findViewById(R.id.event_change_status_button);

        name.setText(event.getName());
        EventStatus eventStatus = EventStatus.fromId(event.getStatus());

        status.setText(eventStatus.getRepresentation());
        status.setTextColor(eventStatus.getColor());

        deadline.setDate(event.getDeadline());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showConfirmPrompt(getContext(), "Warning",
                        "Are you sure you want to delete event " + event.getName() + "? This action is irreversible!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHandler.getInstance().deleteEvent(event.getId());
                                remove(event);
                                Toast.makeText(getContext(), "Successfully removed event", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Changing Status For Project", Toast.LENGTH_LONG).show();
            }
        });

        name.setTextAcceptListener(new OnTextAcceptListener() {
            @Override
            public void onTextEntered(DialogInterface dialog, String input) {
                DatabaseHandler.getInstance().setEventName(event.getId(), input);
            }
        });

        return convertView;
    }
}
