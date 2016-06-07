package com.androidfactory.ayz4sci.permissionhelper;

import android.Manifest;

/**
 * Created by Ayz4sci on 01/06/16.
 */
public class PermissionsGroup {

    // Calendar group.
    public final static String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public final static String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public final static String CALENDAR_GROUP = "Calendar";

    // Camera group.
    public final static String CAMERA = Manifest.permission.CAMERA;
    public final static String CAMERA_GROUP = "Camera";

    // Contacts group.
    public final static String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public final static String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public final static String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public final static String CONTACT_GROUP = "Contact";

    // Location group.
    public final static String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public final static String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public final static String LOCATION_GROUP = "Location";

    // Microphone group.
    public final static String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public final static String MICROPHONE_GROUP = "Microphone";

    // Phone group.
    public final static String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public final static String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public final static String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public final static String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    public final static String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public final static String USE_SIP = Manifest.permission.USE_SIP;
    public final static String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public final static String PHONE_GROUP = "Phone";

    // Sensors group.
    public final static String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public final static String USE_FINGERPRINT = Manifest.permission.USE_FINGERPRINT;
    public final static String SENSORS_GROUP = "Sensors";

    // SMS group.
    public final static String SEND_SMS = Manifest.permission.SEND_SMS;
    public final static String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public final static String READ_SMS = Manifest.permission.READ_SMS;
    public final static String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public final static String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    public final static String READ_CELL_BROADCASTS = "android.permission.READ_CELL_BROADCASTS";
    public final static String SMS_GROUP = "SMS";

    // Storage group.
    public final static String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public final static String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public final static String STORAGE_GROUP = "STORAGE";

    // Bookmarks group.
    public final static String READ_HISTORY_BOOKMARKS = "com.android.browser.permission.READ_HISTORY_BOOKMARKS";
    public final static String WRITE_HISTORY_BOOKMARKS = "com.android.browser.permission.WRITE_HISTORY_BOOKMARKS";
    public final static String BOOKMARKS_GROUP = "Bookmarks";

    public static String getPermissionGroupString(String permission) {
        switch (permission) {
            case READ_CALENDAR:
            case WRITE_CALENDAR:
                return CALENDAR_GROUP;

            case CAMERA:
                return CALENDAR_GROUP;

            case READ_CONTACTS:
            case WRITE_CONTACTS:
            case GET_ACCOUNTS:
                return CONTACT_GROUP;

            case ACCESS_COARSE_LOCATION:
            case ACCESS_FINE_LOCATION:
                return LOCATION_GROUP;

            case RECORD_AUDIO:
                return MICROPHONE_GROUP;

            case READ_PHONE_STATE:
            case CALL_PHONE:
            case READ_CALL_LOG:
            case WRITE_CALL_LOG:
            case ADD_VOICEMAIL:
            case USE_SIP:
            case PROCESS_OUTGOING_CALLS:
                return PHONE_GROUP;

            case BODY_SENSORS:
            case USE_FINGERPRINT:
                return SENSORS_GROUP;

            case READ_SMS:
            case SEND_SMS:
            case RECEIVE_SMS:
            case RECEIVE_WAP_PUSH:
            case READ_CELL_BROADCASTS:
            case RECEIVE_MMS:
                return SMS_GROUP;

            case READ_EXTERNAL_STORAGE:
            case WRITE_EXTERNAL_STORAGE:
                return STORAGE_GROUP;

            case WRITE_HISTORY_BOOKMARKS:
            case READ_HISTORY_BOOKMARKS:
                return BOOKMARKS_GROUP;
        }

        return "";
    }
}
