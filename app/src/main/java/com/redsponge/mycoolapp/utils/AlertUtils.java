package com.redsponge.mycoolapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertUtils {


    public static void showAlert(Context ctx, String title, String content, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(content)
                .setNeutralButton("OK", listener)
                .show();
    }

}
