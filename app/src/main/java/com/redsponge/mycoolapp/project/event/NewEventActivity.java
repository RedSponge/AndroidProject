package com.redsponge.mycoolapp.project.event;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.db.DatabaseHandler;
import com.redsponge.mycoolapp.project.Project;
import com.redsponge.mycoolapp.utils.AbstractActivity;
import com.redsponge.mycoolapp.utils.Constants;
import com.redsponge.mycoolapp.utils.DateTextView;

public class NewEventActivity extends AbstractActivity {

    private EditText nameInput;
    private DateTextView dateInput;
    private Project project;

    @Override
    protected void initialize() {
        setContentView(R.layout.activity_new_event);
        project = (Project) getIntent().getExtras().get(Constants.EXTRA_PROJECT_OBJ);

        nameInput = (EditText) findViewById(R.id.event_name_input);
        dateInput = (DateTextView) findViewById(R.id.event_deadline_input);
    }

    public void createEvent(View view) {
        String name = nameInput.getText().toString();
        long date = dateInput.getAsMilliseconds();

        Event event = new Event(project.getId(), name, EventStatus.TO_DO.getId(), date);
        DatabaseHandler.getInstance().addEvent(event);
        finish();

        Toast.makeText(this, "Successfully created event!", Toast.LENGTH_LONG).show();
    }
}
