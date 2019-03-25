package com.redsponge.mycoolapp.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.redsponge.mycoolapp.R;

public class LoginUtils {

    /**
     * Hashes a password
     * @param pw The password to hash
     * @return The hashed password
     */
    public static int hashPw(String pw) {
        int hash = 7;
        for (int i = 0; i < pw.length(); i++) {
            hash = hash*31 + pw.charAt(i);
        }
        return hash;
    }

    public static void registerCurrentUser(Context ctx, int id) {
        SharedPreferences sp = ctx.getSharedPreferences(ctx.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        sp.edit().putInt("loggedInUser", id).apply();
    }

    public static void clearCurrentUser(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(ctx.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        sp.edit().remove("loggedInUser").apply();
    }

    public static int getCurrentUser(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(ctx.getString(R.string.shared_preferences_name), Context.MODE_PRIVATE);
        return sp.getInt("loggedInUser", -1);
    }
}