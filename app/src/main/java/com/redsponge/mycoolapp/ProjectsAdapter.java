package com.redsponge.mycoolapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectsAdapter extends ArrayAdapter<Project> {

    public ProjectsAdapter(Context context, ArrayList<Project> users) {
        super(context, R.layout.item_project, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Project project = getItem(position);

        if(project == null) {
            throw new RuntimeException("Project Cannot Be Null!");
        }

        if(convertView == null) {
            Log.i(getClass().getName(), "Creating a new view!");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent, false);
        }

        TextView name = convertView.findViewById(R.id.projectName);
        TextView description = convertView.findViewById(R.id.projectDescription);
        Button enter = convertView.findViewById(R.id.gotoButton);

        enter.setOnClickListener(v -> buttonClicked(project));

        name.setText(project.name);
        description.setText(project.description);

        return convertView;
    }

    private void buttonClicked(Project project) {
        Log.i(getClass().getName(), "Button Clicked!");
        getContext().startActivity(new Intent(getContext(), ProjectActivity.class) {{
            putExtra(Const.EXTRA_PROJECT, project);
        }});
    }
}
