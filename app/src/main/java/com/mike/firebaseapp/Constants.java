package com.mike.firebaseapp;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Mike on 4/13/2016.
 * Holds final constant values for app variables.
 */
public class Constants {

    public static final String FIREBASE_URL = "https://shining-fire-8756.firebaseio.com/";
    public static final String FIREBASE_URL_PEOPLE = "People";

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
}
