package com.redsponge.mycoolapp.utils.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public interface OnTextAcceptListener {
    /**
     * Called when an input prompt is accepted, this won't be called if nothing was entered!
     * @param dialog The dialog
     * @param input The text entered
     */
    void onTextEntered(DialogInterface dialog, String input);
}
