package com.redsponge.mycoolapp.project.event;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.project.Project;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.Constants;

import java.util.ArrayList;

/**
 * The activity which shows a list of events, and lets the user configure them (using an {@link EventAdapter})
 */
public class EventsActivity extends AbstractActivity {

    private Project project;
    private ListView eventList;
    private EventAdapter eventAdapter;
    private CheckBox showDone;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_events);
        this.project = (Project) getIntent().getExtras().get(Constants.EXTRA_PROJECT_OBJ);
        this.eventList = (ListView) findViewById(R.id.event_list);
        this.eventAdapter = new EventAdapter(this);
        this.eventList.setAdapter(this.eventAdapter);
        this.showDone = (CheckBox) findViewById(R.id.events_show_done);

        this.showDone.setChecked(true);

        this.showDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateEventAdapter();
            }
        });

        updateEventAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventAdapter();
    }

    private void updateEventAdapter() {
        boolean loadDone = this.showDone.isChecked();

        this.eventAdapter.clear();
        this.eventAdapter.addAll(DatabaseHandler.getInstance().getEventsForProject(project.getId(), !loadDone ? EventStatus.DONE.getId() : -1));
    }

    public void addEvent(View view) {
        Event newEvent = new Event(project.getId(), "Event " + (eventAdapter.getCount() + 1), EventStatus.TO_DO.getId(), System.currentTimeMillis() - 1000);
        db.addEvent(newEvent);
        updateEventAdapter();
    }
}
