package com.redsponge.mycoolapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.utils.alert.AlertUtils;
import com.redsponge.mycoolapp.utils.alert.OnTextAcceptListener;

public class EditableTextView extends TextView {

    private OnTextAcceptListener textAcceptListener;
    private String title;

    public EditableTextView(Context context) {
        super(context);
        initialize();
    }

    public EditableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
        initialize();
    }

    public EditableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(attrs);
        initialize();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray arr = getContext().obtainStyledAttributes(attrs, R.styleable.EditableTextView);
        title = arr.getString(R.styleable.EditableTextView_alertTitle);
        if(title == null) {
            title = "Editable Text View";
        }
        arr.recycle();
    }

    private void initialize() {
        this.setClickable(true);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertUtils.showTextPrompt(getContext(), title, new OnTextAcceptListener() {
                    @Override
                    public void onTextEntered(DialogInterface dialog, String input) {
                        setText(input);
                        if(textAcceptListener != null) {
                            textAcceptListener.onTextEntered(dialog, input);
                        }
                    }
                }, null, (String) getText(), false, null);
            }
        });
    }

    public void setTextAcceptListener(OnTextAcceptListener textAcceptListener) {
        this.textAcceptListener = textAcceptListener;
    }
}
