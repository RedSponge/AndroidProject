package com.redsponge.mycoolapp;

public class LoginUtils {

    public static int hashPw(String pw) {
        int hash = 7;
        for (int i = 0; i < pw.length(); i++) {
            hash = hash*31 + pw.charAt(i);
        }
        return hash;
    }

}
