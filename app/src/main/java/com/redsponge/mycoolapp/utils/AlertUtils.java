package com.redsponge.mycoolapp.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertUtils {


    public static void showAlert(Context ctx, String title, String content) {
        new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setMessage(content)
                .setNeutralButton("OK", null)
                .show();
    }

}
