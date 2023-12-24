package com.example.tripster.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tripster.model.User;
import com.example.tripster.model.UserType;

public class SharedPreferencesManager {
    private static final String PREFERENCE_NAME = "MyAppPreferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_ID = "user_id";
    private static final String PERSON_ID = "person_id";
    private static final String KEY_JWT = "jwt";


    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserInfo(Context context, String email, UserType userType, Long user_id,Long person_id ,String jwt) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USER_ROLE, userType.toString());
        editor.putLong(KEY_ID, user_id);
        editor.putLong(PERSON_ID, person_id);
        editor.putString(KEY_JWT, jwt);
        editor.apply();
    }

    public static User getUserInfo(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        String email = preferences.getString(KEY_EMAIL, "");
        String userRole = preferences.getString(KEY_USER_ROLE, UserType.ADMIN.toString());
        Long userid = preferences.getLong(KEY_ID, 0);
        Long personId= preferences.getLong(PERSON_ID,0);
        String jwt = preferences.getString(KEY_JWT, "");
        return new User( userid, email,  UserType.valueOf(userRole),personId,jwt);
    }

    public static void clearUserInfo(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_USER_ROLE);
        editor.remove(KEY_ID);
        editor.remove(PERSON_ID);
        editor.remove(KEY_JWT);
        editor.apply();
    }
}
