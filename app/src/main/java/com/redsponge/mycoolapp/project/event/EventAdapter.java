package com.redsponge.mycoolapp.project.event;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.utils.alert.OnSpinnerAcceptListener;
import com.redsponge.mycoolapp.utils.views.DateTextView;
import com.redsponge.mycoolapp.utils.views.EditableTextView;
import com.redsponge.mycoolapp.utils.alert.AlertUtils;
import com.redsponge.mycoolapp.utils.alert.OnTextAcceptListener;
import com.redsponge.mycoolapp.utils.views.TVDateChangedListener;

import java.util.logging.Logger;

/**
 * An event adapter for a {@link android.widget.ListView}.
 * shows events, and allows changing and deleting them
 */
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
        if(event == null) {
            Log.wtf("EventAdapter", "Event was null! got position " + position);
            return null;
        }

        EditableTextView name = (EditableTextView) convertView.findViewById(R.id.event_name);
        final TextView status = (TextView) convertView.findViewById(R.id.event_status);
        DateTextView deadline = (DateTextView) convertView.findViewById(R.id.event_deadline);

        Button delete = (Button) convertView.findViewById(R.id.event_delete_button);

        name.setText(event.getName());
        final EventStatus eventStatus = EventStatus.fromId(event.getStatus());
        if(eventStatus == null) {
            Log.wtf("EventAdapter", "Event status was null with event " + event + ", deleting it to prevent anything else from happening!");
            DatabaseHandler.getInstance().deleteEvent(event.getId());
            return null;
        }

        status.setText(eventStatus.getRepresentation());
        status.setTextColor(eventStatus.getColor());
        status.setClickable(true);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showSpinnerPrompt(getContext(), "Change Status", new EventStatusAdapter(getContext()), new OnSpinnerAcceptListener<EventStatus>() {
                    @Override
                    public void accept(EventStatus choice, int choiceId) {
                        choice.displayOnTV(status);
                        event.setStatus(choice.getId());
                        DatabaseHandler.getInstance().setEventStatus(event.getId(), choice.getId());
                    }
                }, event.getStatus());
            }
        });

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

        name.setTextAcceptListener(new OnTextAcceptListener() {
            @Override
            public void onTextEntered(DialogInterface dialog, String input) {
                Log.i("EventAdapter", "Accepted text " + input);
                DatabaseHandler.getInstance().setEventName(event.getId(), input);
            }
        });

        deadline.setDate(event.getDeadline());
        deadline.setDateChangedListener(new TVDateChangedListener() {
            @Override
            public void changed(long millis) {
                DatabaseHandler.getInstance().setEventDeadline(event.getId(), millis);
            }
        });

        return convertView;
    }
}
