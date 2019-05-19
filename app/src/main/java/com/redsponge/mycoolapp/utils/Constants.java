package com.redsponge.mycoolapp.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    public static final int CATEGORY_ALL_ID = -1;

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int USERNAME_MIN_LENGTH = 3;
    public static final float MAX_IMAGE_SIZE = 480;

    public static final String EXTRA_PROJECT_ID = "project_id";
    public static final String EXTRA_PROJECT_OBJ = "project_obj";
    public static final String EXTRA_USER_ID = "user_id";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
}
