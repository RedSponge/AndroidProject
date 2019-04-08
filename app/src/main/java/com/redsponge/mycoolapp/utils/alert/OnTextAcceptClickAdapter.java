package com.redsponge.mycoolapp.utils.alert;

import android.content.DialogInterface;
import android.widget.EditText;

public class OnTextAcceptClickAdapter implements DialogInterface.OnClickListener {

    private EditText editText;
    private OnTextAcceptListener listener;

    public OnTextAcceptClickAdapter(EditText editText, OnTextAcceptListener listener) {
        this.editText = editText;
        this.listener = listener;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String input = editText.getText().toString();
        if(!input.isEmpty()) {
            listener.onTextEntered(dialog, input);
        }
    }
}
