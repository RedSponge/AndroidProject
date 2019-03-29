package com.redsponge.mycoolapp.login;

import android.content.Context;
import android.content.SharedPreferences;

import com.redsponge.mycoolapp.R;
import com.redsponge.mycoolapp.utils.Constants;

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

    /**
     * Checks if a password is valid, by the following rules:
     * 1. A password must be at least {@link Constants#PASSWORD_MIN_LENGTH} characters long
     * 2. A password must have at least 1 digit in it
     * @param pw The password to check
     * @return Is the password valid
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isPasswordValid(String pw) {
        return !pw.isEmpty() && pw.length() >= Constants.PASSWORD_MIN_LENGTH && pw.matches(".*\\d.*");
    }

    public static boolean isUsernameValid(String username) {
        return !username.isEmpty() && username.length() >= Constants.USERNAME_MIN_LENGTH;
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