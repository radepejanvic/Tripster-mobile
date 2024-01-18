package com.example.tripster.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tripster.model.Settings;
import com.example.tripster.model.User;
import com.example.tripster.model.enums.UserType;

public class SharedPreferencesManager {
    private static final String PREFERENCE_NAME = "MyAppPreferences";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_ID = "user_id";
    private static final String PERSON_ID = "person_id";
    private static final String KEY_JWT = "jwt";

    private static final String KEY_SETTINGS_ID = "settings_id";
    private static final String KEY_RESERVATION_NOTIFICATIONS = "reservation_notifications";
    private static final String KEY_REVIEW_NOTIFICATIONS = "review_notifications";
    private static final String KEY_ACCOMMODATION_REVIEW_NOTIFICATIONS = "accommodation_review_notifications";


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

    public static void saveSettings(Context context, Settings settings) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(KEY_SETTINGS_ID, settings.getId());
        editor.putBoolean(KEY_RESERVATION_NOTIFICATIONS, settings.isReservationNotification());
        editor.putBoolean(KEY_REVIEW_NOTIFICATIONS, settings.isReviewNotification());
        editor.putBoolean(KEY_ACCOMMODATION_REVIEW_NOTIFICATIONS, settings.isAccommodationReviewNotification());
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

    public static Settings getSettings(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        Long settingsId = preferences.getLong(KEY_SETTINGS_ID, 0);
        Long userid = preferences.getLong(KEY_ID, 0);
        Boolean reservationNotifications = preferences.getBoolean(KEY_RESERVATION_NOTIFICATIONS, true);
        Boolean reviewNotifications = preferences.getBoolean(KEY_REVIEW_NOTIFICATIONS, true);
        Boolean accommodationReviewNotifications = preferences.getBoolean(KEY_ACCOMMODATION_REVIEW_NOTIFICATIONS, true);
        return new Settings(settingsId, userid, reservationNotifications, reviewNotifications, accommodationReviewNotifications);
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

    public static void clearSettings(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(KEY_SETTINGS_ID);
        editor.remove(KEY_RESERVATION_NOTIFICATIONS);
        editor.remove(KEY_REVIEW_NOTIFICATIONS);
        editor.remove(KEY_ACCOMMODATION_REVIEW_NOTIFICATIONS);
        editor.apply();
    }
}
