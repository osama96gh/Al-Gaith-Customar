package com.example.al_gaith_customar.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.Application;
import com.example.al_gaith_customar.Data.ApplicationData;
import com.example.al_gaith_customar.Data.ApplicationField;
import com.example.al_gaith_customar.Data.ApplicationType;
import com.example.al_gaith_customar.Utility.QueryUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class GeneralUtility {

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
                error = baseJsonResponse.getString("error");
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
        String error = "خطأ في الاتصال بالخادم";
        return response;
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
                    ApplicationField applicationField = gson.fromJson(contentJsonArry.getJSONObject(i).toString(), ApplicationField.class);
                    appList.add(applicationField);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return appList;
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

    public static String signUp(Context context, String first, String last, String
            userName, String password, String password_config, String mobile, String phon, String
                                        personal, String idFront, String idBehind) {
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
        uriBuilder.appendQueryParameter("profile_picture", personal);
        uriBuilder.appendQueryParameter("id_first_picture", idFront);
        uriBuilder.appendQueryParameter("id_second_picture", idBehind);

        Log.println(Log.ASSERT, "link length", "" + uriBuilder.toString().length());
        String response = QueryUtils.fetchData(uriBuilder.toString());
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
                error = baseJsonResponse.getString("error");
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

}
