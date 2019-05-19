package com.redsponge.mycoolapp.project.category;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.redsponge.mycoolapp.db.DatabaseHandler;

import java.util.Locale;

public class CategoryAdapter extends ArrayAdapter<Category> implements SpinnerAdapter {
    public CategoryAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        Category cat = getItem(position);
        if(cat == null) {
            Log.wtf("CategoryAdapter", "Category was null!");
            return convertView;
        }

        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(String.format(Locale.UK, "%s (%d)", cat.getName(), DatabaseHandler.getInstance().getProjectAmountInCategory(cat.getId(), cat.getUser())));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
