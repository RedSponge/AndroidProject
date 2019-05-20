package com.redsponge.mycoolapp.utils.alert;

import android.content.DialogInterface;
import android.widget.Spinner;

public class OnSpinnerAcceptClickAdapter<T> implements DialogInterface.OnClickListener {

    private Spinner spinner;
    private OnSpinnerAcceptListener<T> onSpinnerAcceptListener;

    public OnSpinnerAcceptClickAdapter(Spinner spinner, OnSpinnerAcceptListener<T> onSpinnerAcceptListener) {
        this.spinner = spinner;
        this.onSpinnerAcceptListener = onSpinnerAcceptListener;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onClick(DialogInterface dialog, int which) {
        T choice = (T) spinner.getSelectedItem();
        if(choice != null) {
            onSpinnerAcceptListener.accept(choice, spinner.getSelectedItemPosition());
        }
    }
}
