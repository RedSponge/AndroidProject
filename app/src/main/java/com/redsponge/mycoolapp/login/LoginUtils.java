package com.redsponge.mycoolapp.login;

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

}