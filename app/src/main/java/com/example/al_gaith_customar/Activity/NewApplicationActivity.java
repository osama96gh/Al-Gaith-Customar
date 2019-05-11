package com.example.al_gaith_customar.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
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


import java.util.ArrayList;

import static android.view.Gravity.CENTER;

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
        linearLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        TextView titleTV = new TextView(this);
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
        editTextLayout.setId(applicationField.id);
        editTextLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        rootView.addView(editTextLayout);

        EditText editText = new EditText(this);
        editText.setHint(applicationField.field_name);
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
        //editTextLayout.setId(id);
        editTextLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        parentView.addView(editTextLayout);

        EditText editText = new EditText(this);
        editText.setHint(applicationField.field_name);
        // setEditTextAttributes(editText);
        editTextLayout.addView(editText);

        //addLineSeperator();
    }

    private void addImageField(final ApplicationField applicationField) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout linearLayout = new LinearLayout(NewApplicationActivity.this);
        linearLayout.setPadding(convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4), convertDpToPixel(4));
        linearLayout.setLayoutParams(getDefaultParam(4));
        linearLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        linearLayout.setId(applicationField.id);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleTV = new TextView(this);
        titleTV.setText(applicationField.field_name);
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_photo));
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
        linearLayout.setBackground(getResources().getDrawable(R.drawable.fram_primary));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout container = new LinearLayout(this);

        container.setOrientation(LinearLayout.VERTICAL);
        container.setLayoutParams(getDefaultParam(4));
        Button addButton = new Button(this);
        addButton.setLayoutParams(getDefaultParam(4));
        addButton.setText("إضافة");
        TextView titleTV = new TextView(this);
        titleTV.setText(applicationField.field_name);
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
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
            if (applicationData.field_type.matches("text")) {
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
                Toast.makeText(NewApplicationActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewApplicationActivity.this, ApplicationActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(NewApplicationActivity.this, "حصل خطأ ما", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        Uri imageUri;
        Bitmap imageBitMap;

        if (resultCode == Activity.RESULT_OK) {

            imageUri = data.getData();
            imageBitMap = GeneralUtility.getImageFromUri(NewApplicationActivity.this, imageUri);
            String base64 = GeneralUtility.getBase64(imageBitMap);
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).field_id == requestCode) {
                    dataList.get(i).value = base64;
                    break;
                }
            }

//            pickIdBehindPhoto.setImageBitmap(imageBitMap);
//            pickIdBehindPhoto.setColorFilter(null);
//            ImageViewCompat.setImageTintList(pickIdBehindPhoto, null);
            ((ImageView) (((LinearLayout) rootView.findViewById(requestCode)).getChildAt(1))).setImageBitmap(imageBitMap);
        }
    }
}
