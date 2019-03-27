package com.redsponge.mycoolapp.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
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

import java.util.ArrayList;

public class ProjectsAdapter extends ArrayAdapter<Project> {

    private int currentUser;
    private DatabaseHandler db;

    public ProjectsAdapter(Context context, ArrayList<Project> users, int currentUser, DatabaseHandler db) {
        super(context, R.layout.item_project, users);
        this.currentUser = currentUser;
        this.db = db;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Project project = getItem(position);

        if(project == null) {
            throw new RuntimeException("Project Cannot Be Null!");
        }

        if(convertView == null) {
            Log.i(getClass().getName(), "Creating a new view!");
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.projectName);
        TextView description = (TextView) convertView.findViewById(R.id.projectDescription);
        Button enter = (Button) convertView.findViewById(R.id.gotoButton);
        ImageView image = (ImageView) convertView.findViewById(R.id.projectIcon);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClicked(project);
            }
        });

        name.setText(project.name);

        String descriptionText = project.description;
        if(descriptionText.length() > 30) {
            descriptionText = descriptionText.substring(0, 30) + "...";
        }

        description.setText(descriptionText);

        String icon = db.getIcon(project.id);
        if(icon != null) {
            image.setImageBitmap(ImageUtils.decode(icon));
        }

        return convertView;
    }

    private void buttonClicked(Project project) {
        Log.i(getClass().getName(), "Button Clicked!");
        Intent intent = new Intent(getContext(), ProjectActivity.class);
        intent.putExtra("project", project);
        intent.putExtra("currentUser", currentUser);

        getContext().startActivity(intent);
    }
}
