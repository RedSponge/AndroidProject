package com.redsponge.mycoolapp.utils.alert;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class PopupTextWatcher implements TextWatcher {

    private EditText editText;

    public PopupTextWatcher() {}

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChanged(s, editText);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public abstract void onTextChanged(CharSequence s, EditText text);
}
