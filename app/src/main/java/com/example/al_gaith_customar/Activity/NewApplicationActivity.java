package com.example.al_gaith_customar.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationData;
import com.example.al_gaith_customar.Data.ApplicationField;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static android.view.Gravity.CENTER;

class ImageInfo {
    public String path;
    public String fiald_id;

    public ImageInfo(String path, String fiald_id) {
        this.path = path;
        this.fiald_id = fiald_id;
    }

    public String getKey() {
        return "image-" + fiald_id;
    }
}

public class NewApplicationActivity extends AppCompatActivity {
    int appId = 0;
    Toolbar toolbar;
    ArrayList<ApplicationField> fieldList = new ArrayList<>();
    ArrayList<ApplicationData> dataList = new ArrayList<>();

    LinearLayout rootView;
    LinearLayout errorLayout;
    TextView errorTV;
    ProgressBar uploadPB;
    ProgressBar loadPB;
    ArrayList<ImageInfo> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);
        rootView = findViewById(R.id.application_field_root);
        errorLayout = findViewById(R.id.error_ll);
        errorTV = findViewById(R.id.error_tv);
        loadPB = findViewById(R.id.load_progress_pb);
        uploadPB = findViewById(R.id.upload_progress_layout);

        appId = getIntent().getIntExtra(AppData.APPLICATION_ID_KEY, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(AppData.APPLICATION_TITLE_KEY));
        LoadApplicationFields loadApplicationFields = new LoadApplicationFields();
        loadApplicationFields.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    private void addEnumField(final ApplicationField applicationField) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        LinearLayout linearLayout = new LinearLayout(NewApplicationActivity.this);
        linearLayout.setLayoutParams(params);
        linearLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(applicationField.id);
        //linearLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        TextView titleTV = new TextView(this);
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");
        titleTV.setTypeface(type);
        titleTV.setTextColor(getResources().getColor(R.color.colorAccent));
        titleTV.setText(applicationField.field_name);
        RadioGroup radioGroup = new RadioGroup(this);

        radioGroup.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(titleTV);
        linearLayout.addView(radioGroup);
        rootView.addView(linearLayout);
        if (applicationField.enum_values != null)
            for (String choice : applicationField.enum_values) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(choice);
                radioButton.setTypeface(type);
                radioButton.setTextSize(18);
                radioButton.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                radioGroup.addView(radioButton);
                // setRadioButtonAttributes(radioButton);
            }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                String choise = (String) radioButton.getText();
                if (applicationField.children != null) {
                    if (choise.matches(applicationField.positive_value)) {
                        for (int id : applicationField.children) {
                            rootView.findViewById(id).setVisibility(View.VISIBLE);
                        }
                    } else {
                        for (int id : applicationField.children) {
                            rootView.findViewById(id).setVisibility(View.GONE);
                        }
                    }
                }
                for (int i = 0; i < dataList.size(); i++) {
                    if (dataList.get(i).field_id == applicationField.id) {
                        dataList.get(i).value = choise;
                    }
                }

            }
        });
        // addLineSeperator();
    }


    private void addTextField(ApplicationField applicationField) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        TextInputLayout editTextLayout = new TextInputLayout(this);
        editTextLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        editTextLayout.setLayoutParams(params);
        editTextLayout.setDefaultHintTextColor(getResources().getColorStateList(R.color.colorAccent));

        editTextLayout.setId(applicationField.id);
        //editTextLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        rootView.addView(editTextLayout);

        EditText editText = new EditText(this);
        editText.setHint(applicationField.field_name);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");
        editText.setTypeface(type);
        editText.setGravity(Gravity.RIGHT);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        // setEditTextAttributes(editText);
        editTextLayout.addView(editText);

        //addLineSeperator();
    }

    private void addTextField(ApplicationField applicationField, LinearLayout parentView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        TextInputLayout editTextLayout = new TextInputLayout(this);
        editTextLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        editTextLayout.setLayoutParams(params);
        editTextLayout.setDefaultHintTextColor(getResources().getColorStateList(R.color.colorAccent));

        //editTextLayout.setId(id);
        // editTextLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        parentView.addView(editTextLayout);

        EditText editText = new EditText(this);
        editText.setHint(applicationField.field_name);
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");
        editText.setTypeface(type);
        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));
        editText.setGravity(Gravity.RIGHT);
        // setEditTextAttributes(editText);
        editTextLayout.addView(editText);

        //addLineSeperator();
    }

    private void addImageField(final ApplicationField applicationField) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout linearLayout = new LinearLayout(NewApplicationActivity.this);
        linearLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        linearLayout.setLayoutParams(getDefaultParam(4));
        // linearLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        linearLayout.setId(applicationField.id);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleTV = new TextView(this);
        titleTV.setTextColor(getResources().getColor(R.color.colorAccent));
        titleTV.setText(applicationField.field_name);
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");
        titleTV.setTypeface(type);

        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo));
        imageView.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        imageView.setPadding(convertDpToPixel(2), convertDpToPixel(2), convertDpToPixel(2), convertDpToPixel(2));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                convertDpToPixel(250));
        imageView.setLayoutParams(params);

        linearLayout.addView(titleTV);
        linearLayout.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(applicationField.id);
            }
        });
        rootView.addView(linearLayout);

        //addLineSeperator();
    }

    private void addMultibleField(final ApplicationField applicationField) {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(getDefaultParam(4));
        linearLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        linearLayout.setId(applicationField.id);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout container = new LinearLayout(this);

        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        container.setLayoutParams(getDefaultParam(4));
        Button addButton = new Button(this);
        addButton.setLayoutParams(getDefaultParam(4));
        addButton.setText("إضافة");
        Typeface type = Typeface.createFromAsset(getAssets(), "tajawal.ttf");

        addButton.setTypeface(type);
        addButton.setTextColor(getResources().getColor(R.color.colorAccent));
        TextView titleTV = new TextView(this);
        titleTV.setText(applicationField.field_name);

        titleTV.setTypeface(type);
        titleTV.setTextColor(getResources().getColor(R.color.colorAccent));

        addButton.setGravity(CENTER);
        linearLayout.addView(titleTV);
        linearLayout.addView(container);
        linearLayout.addView(addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int id : applicationField.children) {
                    for (ApplicationField appField : fieldList) {
                        if (appField.id == id) {
                            if (appField.field_type.matches("text")) {
                                addTextField(appField, container);
                            }
                        }
                    }
                }
            }
        });
        rootView.addView(linearLayout);

    }

    LinearLayout.LayoutParams getDefaultParam(float marging) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(convertDpToPixel(marging), convertDpToPixel(marging), convertDpToPixel(marging), convertDpToPixel(marging));
        return params;
    }

    //This function to convert DPs to pixels
    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    void pickImage(int requestCode) {
        requstReadPermission();
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, requestCode);
    }

    void requstReadPermission() {
        ActivityCompat.requestPermissions(NewApplicationActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(NewApplicationActivity.this, "فشل التطبيق في الحصول على إذن القراءة", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void sendApplication(View view) {
        SendApplicationData sendApplicationData = new SendApplicationData();
        sendApplicationData.execute();

    }

    String getTextFromTextInputLayout(TextInputLayout textInputLayout) {
        String value = String.valueOf(textInputLayout.getEditText().getText());
        return value;
    }

    int[] getChildrenId(int id) {
        for (ApplicationField applicationField : fieldList) {
            if (applicationField.id == id) {
                return applicationField.children;
            }
        }
        return null;
    }

    ApplicationField getField(int id) {
        for (ApplicationField applicationField : fieldList) {
            if (applicationField.id == id) {
                return applicationField;
            }
        }
        return null;
    }

    void updateData() {
        int size = dataList.size();
        for (int i = 0; i < size; i++) {
            ApplicationData applicationData = dataList.get(i);
            if (applicationData.field_type.matches("text") || applicationData.field_type.matches("number")) {
                TextInputLayout textInputLayout = rootView.findViewById(applicationData.field_id);
                dataList.get(i).value = getTextFromTextInputLayout(textInputLayout);
            } else if (applicationData.field_type.matches("button")) {
                int[] childrenID = getChildrenId(applicationData.field_id);
                LinearLayout continer1 = rootView.findViewById(applicationData.field_id);
                LinearLayout mainContainer = (LinearLayout) continer1.getChildAt(1);
                int elementCont = mainContainer.getChildCount();
                for (int j = 0; j < elementCont; j++) {
                    TextInputLayout textInputLayout = (TextInputLayout) mainContainer.getChildAt(j);
                    ApplicationField applicationField = getField(childrenID[j % childrenID.length]);
                    ApplicationData data = new ApplicationData(applicationField.id, applicationField.field_type);
                    data.value = getTextFromTextInputLayout(textInputLayout);
                    Log.println(Log.ASSERT, "text", getTextFromTextInputLayout(textInputLayout));
                    dataList.add(data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ApplicationActivity.class);
        startActivity(intent);
        finish();
    }

    String getDataAsJSONArray() {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(dataList, new TypeToken<ArrayList<ApplicationData>>() {
        }.getType());
        if (!element.isJsonArray()) {
            // fail appropriately
            Log.println(Log.ASSERT, "error", "error convert list to json");
        }

        JsonArray jsonArray = element.getAsJsonArray();


        Log.println(Log.ASSERT, "array", "" + jsonArray.toString());
        return jsonArray.toString();
    }

    class LoadApplicationFields extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getApplicationFieldData(NewApplicationActivity.this, AppData.authType + AppData.userToken, "" + appId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loadPB.setVisibility(View.GONE);

            fieldList = GeneralUtility.parseApplicationField(s);
            for (ApplicationField applicationField : fieldList) {
                dataList.add(new ApplicationData(applicationField.id, applicationField.field_type));

                if (applicationField.field_type.matches("enum")) {
                    addEnumField(applicationField);
                } else if (applicationField.field_type.matches("text") || applicationField.field_type.matches("number")) {
                    addTextField(applicationField);
                } else if (applicationField.field_type.matches("image")) {
                    addImageField(applicationField);
                } else if (applicationField.field_type.matches("button")) {
                    addMultibleField(applicationField);
                }
            }

            for (ApplicationField applicationField : fieldList) {
                if (applicationField.children != null) {
                    for (int i : applicationField.children) {
                        rootView.findViewById(i).setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    class SendApplicationData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateData();
            uploadPB.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(Void... voids) {

            return GeneralUtility.sendApplicationData(NewApplicationActivity.this, AppData.authType + AppData.userToken, "" + appId, getDataAsJSONArray());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploadPB.setVisibility(View.GONE);
            if (s != null) {
                String success = GeneralUtility.getSuccessMassage(s);
                if (success != null) {
                    try {

                        uploadImage(success);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // onBackPressed();
                } else {
                    String errorMassage = GeneralUtility.getErrorMassage(s);
                    if (errorMassage != null) {
                        errorLayout.setVisibility(View.VISIBLE);
                        errorTV.setText("" + errorMassage);

                    } else {
                        errorLayout.setVisibility(View.VISIBLE);
                        errorTV.setText("خطأ في الإتصال بالخادم!");
                    }
                }
            } else {
                errorLayout.setVisibility(View.VISIBLE);
                errorTV.setText("خطأ في الإتصال بالخادم!");
            }

        }

    }

    void uploadImage(String application_id) throws MalformedURLException, FileNotFoundException {
        MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(NewApplicationActivity.this, AppData.BASIC_URI + "/upload/files");
        for (ImageInfo imageInfo : imageList) {
            multipartUploadRequest.addFileToUpload(imageInfo.path, imageInfo.getKey());
        }

        String uploadId = multipartUploadRequest
                .addParameter("data", "osama")
                .setNotificationConfig(new UploadNotificationConfig())
                .setMaxRetries(2)
                .addParameter("application_id", application_id)
                .addParameter("type", "application")
                .setDelegate(new UploadServiceBroadcastReceiver() {
                    @Override
                    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                        Log.println(Log.ASSERT, "resulte", serverResponse.getBodyAsString());
                        super.onCompleted(context, uploadInfo, serverResponse);

                        onBackPressed();
                    }

                    @Override
                    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                        Log.println(Log.ASSERT, "error", serverResponse.getBodyAsString());
                        super.onError(context, uploadInfo, serverResponse, exception);
                    }

                    @Override
                    public void onCancelled(Context context, UploadInfo uploadInfo) {
                        Log.println(Log.ASSERT, "onCancelled", "" + uploadInfo.getSuccessfullyUploadedFiles().size());

                        super.onCancelled(context, uploadInfo);
                    }
                })
                .startUpload();

    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return path;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        Uri imageUri;
        Bitmap imageBitMap;
        if (resultCode == Activity.RESULT_OK) {

                imageUri = data.getData();
                imageBitMap = GeneralUtility.getImageFromUri(NewApplicationActivity.this, imageUri);

            try {
                File outputDir = this.getCacheDir(); // context being the Activity pointer

                File outputFile = File.createTempFile("osaka", ".jpg", outputDir);
                OutputStream outStream = null;

                outStream = new FileOutputStream(outputFile);
                imageBitMap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
                outStream.close();
                Toast.makeText(getApplicationContext(), "Saved" + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                imageList.add(new ImageInfo(outputFile.getAbsolutePath(), "" + requestCode));
                uploadImage("" + requestCode);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            //  String tempUri = getImageUri(this,imageBitMap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH

            // Log.println(Log.ASSERT,"file path",tempUri);
            //File finalFile = new File(tempUri);
            // imageList.add(new ImageInfo(finalFile.getAbsolutePath(), "" + requestCode));

            // String base64 = GeneralUtility.getBase64(imageBitMap);
//            for (int i = 0; i < dataList.size(); i++) {
//                if (dataList.get(i).field_id == requestCode) {
//                    dataList.get(i).value = base64;
//                    break;
//                }
//            }
//            pickIdBehindPhoto.setImageBitmap(imageBitMap);
//            pickIdBehindPhoto.setColorFilter(null);
//            ImageViewCompat.setImageTintList(pickIdBehindPhoto, null);
            ((ImageView) (((LinearLayout) rootView.findViewById(requestCode)).getChildAt(1))).setImageBitmap(imageBitMap);
        }
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
