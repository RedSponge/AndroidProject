package com.redsponge.mycoolapp.project.event;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


public class EventStatusAdapter extends ArrayAdapter<EventStatus> implements SpinnerAdapter {

    public EventStatusAdapter(Context context) {
        super(context, 0);
        this.addAll(EventStatus.values());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        EventStatus eventStatus = getItem(position);
        if(eventStatus == null) {
            Log.wtf("EventStatusAdapter", "Unknown event status position:" + position);
            return null;
        }

        TextView displayed = (TextView) convertView.findViewById(android.R.id.text1);
        eventStatus.displayOnTV(displayed);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
