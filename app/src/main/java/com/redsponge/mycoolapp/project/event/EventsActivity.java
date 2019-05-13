package com.redsponge.mycoolapp.project.event;

import android.widget.ListView;

import com.redsponge.mycoolapp.R;
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
        this.eventAdapter.add(new Event(1, "Test", 3, 100));

        this.eventList.setAdapter(this.eventAdapter);
    }
}
