package com.example.al_gaith_customar.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import android.util.Base64;
import android.util.Log;

import com.example.al_gaith_customar.Activity.SignUpActivity;
import com.example.al_gaith_customar.Data.Ads;
import com.example.al_gaith_customar.Data.Announcement;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.Application;
import com.example.al_gaith_customar.Data.ApplicationData;
import com.example.al_gaith_customar.Data.ApplicationDate;
import com.example.al_gaith_customar.Data.ApplicationField;
import com.example.al_gaith_customar.Data.ApplicationState;
import com.example.al_gaith_customar.Data.ApplicationType;
import com.example.al_gaith_customar.Data.Massage;
import com.example.al_gaith_customar.Data.Notification;
import com.example.al_gaith_customar.Data.Reply;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GeneralUtility {


    public static String getSuccessMassage(String json) {
        String massage = null;
        JSONObject baseJsonObject = null;
        try {
            baseJsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (baseJsonObject != null) {
            try {
                massage = baseJsonObject.getString("success_message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return massage;
    }

    public static String getErrorMassage(String json) {
        String massage = null;
        JSONObject baseJsonObject = null;
        try {
            baseJsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (baseJsonObject != null) {
            try {
                massage = baseJsonObject.getString("error_message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return massage;
    }

    public static void saveString(String key, String data, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppData.PUBLIC_SHARED_PREFERENCE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, data);
        editor.apply();
    }

    public static String readString(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppData.PUBLIC_SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String data = prefs.getString(key, null);
        return data;
    }

    public static void deleteValue(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppData.PUBLIC_SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String logIn(Context context, String userName, String password) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/consumer/login");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        uriBuilder.appendQueryParameter("username", userName);
        uriBuilder.appendQueryParameter("password", password);

        String response = QueryUtils.fetchData(uriBuilder.toString());
        String error = "خطأ في الاتصال بالخادم";

        JSONObject baseJsonResponse = null;
        try {
            if (response != null)
                baseJsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (baseJsonResponse != null) {


            try {
                JSONObject resultJsonObject = baseJsonResponse.getJSONObject("success");
                String token = resultJsonObject.getString("token");
                String name = resultJsonObject.getString("name");
                String id = resultJsonObject.getString("username");
                String photo = resultJsonObject.getString("profile_picture");
                JSONArray jsonArray = resultJsonObject.getJSONArray("groups");


                saveString(AppData.USER_TOKEN_KEY, token, context);
                saveString(AppData.USER_ID_KEY, id, context);
                saveString(AppData.USER_NAME_KEY, name, context);
                saveString(AppData.USER_PHOTO_KEY, photo, context);
                saveString(AppData.USER_GROUP_JSON_ARRAY_KEY, jsonArray.toString(), context);
                AppData.userToken = token;
                AppData.userName = name;
                AppData.userId = id;
                AppData.userPhotoUrl = photo;


                return "no error";


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                error = baseJsonResponse.getString("error_message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return error;
    }

    public static String getMyApplicationData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/my/applications");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "get application respon", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getAnnouncementData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/announcement");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "announcement response", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getAppAndMassageData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/notifications/new");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "announcement response", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getMyGroupData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/my/groups");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "announcement response", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getMyAdsData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/alresalah/ads");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "announcement response", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getMassageData(Context context, String auth, String massage_id) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/message/responses");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        uriBuilder.appendQueryParameter("id", massage_id);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "announcement response", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getMyApplicationsDate(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/my/dates");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "get application respon", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }


    public static String getMyMassagesData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/messages");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "get massage respon", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getMyNotificationData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/notifications/log");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "get notification respon", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getApplicationTypeData(Context context, String auth) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/send/applications");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);


        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "application type respon", response);

        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getApplicationFieldData(Context context, String auth, String id) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/send/applications/data");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("id", id);
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "app field response", response);
        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String getApplicationDetailsData(Context context, String auth, String id) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/my/application");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("app_id", id);
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "app details response", response);
        String error = "خطأ في الاتصال بالخادم";
        return response;
    }

    public static String sendApplicationData(Context context, String auth, String id, String data) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/get/applications/data");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        uriBuilder.appendQueryParameter("name_id", id);
        uriBuilder.appendQueryParameter("data", data);
        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "send app response", response);
        return response;
    }

    public static String sendMassageData(Context context, String auth, String massageType, String massage) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/messages/store");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        uriBuilder.appendQueryParameter("message_type", massageType);
        uriBuilder.appendQueryParameter("message", massage);
        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "send massage response", response);
        String result = null;
        result = getSuccessMassage(response);
        return result;
    }

    public static String sendTokenData(Context context, String auth, String firebase_id) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI+"/get/firebase/id");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        uriBuilder.appendQueryParameter("firebase_id", firebase_id);
        String response = QueryUtils.fetchData(uriBuilder.toString(), auth);
        Log.println(Log.ASSERT, "send token", "responce : " + response);
        String result = null;
        result = getSuccessMassage(response);
        return result;
    }

    public static ArrayList<Application> parseApplication(String applicationJSON) {
        ArrayList<Application> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray contentJsonArry = null;
        if (baseJsonResponse != null) {

            try {
                contentJsonArry = baseJsonResponse.getJSONArray("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    Application application = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), Application.class);
                    appList.add(application);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<Announcement> parseAnnouncement(String applicationJSON) {
        ArrayList<Announcement> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject contentJsonObject = null;
        if (baseJsonResponse != null) {

            try {
                contentJsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray contentJsonArry = null;
        if (contentJsonObject != null) {
            try {
                contentJsonArry = contentJsonObject.getJSONArray("announcements");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    Announcement announcement = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), Announcement.class);
                    appList.add(announcement);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<Ads> parseAds(String applicationJSON) {
        ArrayList<Ads> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject contentJsonObject = null;
        if (baseJsonResponse != null) {

            try {
                contentJsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray contentJsonArry = null;
        if (contentJsonObject != null) {
            try {
                contentJsonArry = contentJsonObject.getJSONArray("announcements");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    Ads announcement = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), Ads.class);
                    appList.add(announcement);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<Reply> Reply(String applicationJSON) {
        ArrayList<Reply> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject contentJsonObject = null;
        if (baseJsonResponse != null) {

            try {
                contentJsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONArray contentJsonArry = null;
        if (contentJsonObject != null) {
            try {
                contentJsonArry = contentJsonObject.getJSONArray("responses");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    Reply reply = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), Reply.class);
                    appList.add(reply);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<ApplicationDate> parseApplicationsDate(String applicationJSON) {
        ArrayList<ApplicationDate> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray contentJsonArry = null;
        if (baseJsonResponse != null) {

            try {
                contentJsonArry = baseJsonResponse.getJSONArray("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    ApplicationDate applicationDate = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), ApplicationDate.class);
                    appList.add(applicationDate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<Massage> parseMassage(String massageJSON) {
        ArrayList<Massage> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(massageJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject contentJsonObject = null;
        if (baseJsonResponse != null) {
            try {
                contentJsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JSONArray contentJsonArry = null;
        if (contentJsonObject != null) {

            try {
                contentJsonArry = contentJsonObject.getJSONArray("messages");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    Massage massage = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), Massage.class);
                    appList.add(massage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<Notification> parseNotification(String massageJSON) {
        ArrayList<Notification> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(massageJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject contentJsonObject = null;
        if (baseJsonResponse != null) {
            try {
                contentJsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JSONArray contentJsonArry = null;
        if (contentJsonObject != null) {

            try {
                contentJsonArry = contentJsonObject.getJSONArray("notifications");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    Notification notification = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), Notification.class);
                    appList.add(notification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<ApplicationType> parseApplicationType(String applicationTypeJSON) {
        ArrayList<ApplicationType> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationTypeJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray contentJsonArry = null;
        if (baseJsonResponse != null) {

            try {
                contentJsonArry = baseJsonResponse.getJSONArray("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    ApplicationType applicationType = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), ApplicationType.class);
                    appList.add(applicationType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ArrayList<ApplicationField> parseApplicationField(String applicationFieldJSON) {
        ArrayList<ApplicationField> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationFieldJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject baseFiledObject = null;
        if(baseJsonResponse != null){
            try {
                baseFiledObject =baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONArray contentJsonArry = null;
        if (baseFiledObject != null) {
            try {
                contentJsonArry = baseFiledObject.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    ApplicationField applicationField = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), ApplicationField.class);
                    appList.add(applicationField);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static int parseApplicationStepNumber(String applicationFieldJSON) {
        ArrayList<ApplicationField> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationFieldJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject baseFiledObject = null;
        if(baseJsonResponse != null){
            try {
                baseFiledObject =baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        int stepNum=1 ;
        if (baseFiledObject != null) {
            try {
                stepNum = baseFiledObject.getInt("max_step");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stepNum;
    }

    public static ArrayList<ApplicationData> parseApplicationData(String applicationFieldJSON) {
        ArrayList<ApplicationData> appList = new ArrayList<>();

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationFieldJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        if (baseJsonResponse != null) {
            try {
                jsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JSONArray contentJsonArry = null;
        if (jsonObject != null) {
            try {
                contentJsonArry = jsonObject.getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (contentJsonArry != null) {
            Gson gson = new Gson();
            for (int i = 0; i < contentJsonArry.length(); i++) {
                try {
                    ApplicationData applicationData = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), ApplicationData.class);
                    appList.add(applicationData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
    }

    public static ApplicationState parseApplicationStateData(String applicationFieldJSON) {

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(applicationFieldJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        if (baseJsonResponse != null) {
            try {
                jsonObject = baseJsonResponse.getJSONObject("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject contentJsonObject1 = null;
        if (jsonObject != null) {
            try {
                contentJsonObject1 = jsonObject.getJSONObject("application");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ApplicationState applicationState = null;
        if (contentJsonObject1 != null) {
            Gson gson = new Gson();
            applicationState = gson.fromJson(contentJsonObject1.toString(), ApplicationState.class);

        }

        return applicationState;
    }

    public static String signUp(Context context, String first, String last, String
            userName, String password, String password_config, String mobile, String phon) {
        Uri baseUri = Uri.parse(AppData.BASIC_URI + "/consumer/register");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("asso_id", AppData.ASSO_ID);
        uriBuilder.appendQueryParameter("first_name", first);
        uriBuilder.appendQueryParameter("surname", last);
        uriBuilder.appendQueryParameter("username", userName);
        uriBuilder.appendQueryParameter("password", password);
        uriBuilder.appendQueryParameter("c_password", password_config);
        uriBuilder.appendQueryParameter("mobile", mobile);
        uriBuilder.appendQueryParameter("phone", phon);

        String response = QueryUtils.fetchData(uriBuilder.toString());
        Log.println(Log.ASSERT, "sineUp info", "" + uriBuilder.toString().length());

        String error = "خطأ في الاتصال بالخادم";

        JSONObject baseJsonResponse = null;
        try {
            baseJsonResponse = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (baseJsonResponse != null) {
            try {
                JSONObject resultJsonObject = baseJsonResponse.getJSONObject("success");
                String token = resultJsonObject.getString("token");
                String name = resultJsonObject.getString("name");
                String id = resultJsonObject.getString("username");
                String photo = resultJsonObject.getString("profile_picture");

                SignUpActivity.USER_ID = resultJsonObject.getString("user_id");
                saveString(AppData.USER_TOKEN_KEY, token, context);
                saveString(AppData.USER_ID_KEY, id, context);
                saveString(AppData.USER_NAME_KEY, name, context);
                saveString(AppData.USER_PHOTO_KEY, photo, context);
                AppData.userToken = token;
                AppData.userName = name;
                AppData.userId = id;
                AppData.userPhotoUrl = photo;


                return "no error";


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                error = baseJsonResponse.getString("error_message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return error;
    }


    public static Bitmap getImageFromUri(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        // Get the cursor
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        // Set the Image in ImageView after decoding the String
        Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
        Log.println(Log.ASSERT, "image befor commpress", "" + (bitmap.getByteCount() / 1000) + " kb");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        Log.println(Log.ASSERT, "image after commpress", "" + (bitmap.getByteCount() / 1000) + " kb");
        bitmap = getResizedBitmap(bitmap, 400);
        Log.println(Log.ASSERT, "image after resize", "" + (bitmap.getByteCount() / 1000) + " kb");

        return bitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String getBase64(Bitmap bitmap) {
        String encoded = Base64.encodeToString(getByteArray(bitmap), Base64.DEFAULT);
        return encoded;
    }

    public static byte[] getByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }

    public static void subsicribe(final String topic) {

        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed to " + topic;
                        if (!task.isSuccessful()) {
                            msg = "Fail subscribe to " + topic;
                        }
                    }
                });

    }

}
