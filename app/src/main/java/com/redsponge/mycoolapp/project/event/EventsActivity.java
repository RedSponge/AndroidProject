package com.redsponge.mycoolapp.project.event;

import com.redsponge.mycoolapp.project.Project;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.Constants;

public class EventsActivity extends AbstractActivity {

    private Project project;

    @Override
    protected void initialize() {
        this.project = (Project) getIntent().getExtras().get(Constants.EXTRA_PROJECT_OBJ);

    }
}
