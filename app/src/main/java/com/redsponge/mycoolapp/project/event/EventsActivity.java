package com.redsponge.mycoolapp.project.event;

import android.view.View;
import android.widget.ListView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.project.Project;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.Constants;

public class EventsActivity extends AbstractActivity {

    private Project project;
    private ListView eventList;
    private EventAdapter eventAdapter;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_events);
        this.project = (Project) getIntent().getExtras().get(Constants.EXTRA_PROJECT_OBJ);
        this.eventList = (ListView) findViewById(R.id.event_list);
        this.eventAdapter = new EventAdapter(this);
        this.eventList.setAdapter(this.eventAdapter);

        updateEventAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventAdapter();
    }

    private void updateEventAdapter() {
        this.eventAdapter.clear();
        this.eventAdapter.addAll(DatabaseHandler.getInstance().getEventsForProject(project.getId()));
    }

    public void addEvent(View view) {
        Event newEvent = new Event(project.getId(), "Event " + (eventAdapter.getCount() + 1), EventStatus.TO_DO.getId(), System.currentTimeMillis() - 1000);
        db.addEvent(newEvent);
        this.eventAdapter.add(newEvent);
    }
}
