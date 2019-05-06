package com.example.al_gaith_customar.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationField;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class NewApplicationActivity extends AppCompatActivity {
    int appId = 0;
    Toolbar toolbar;
    ArrayList<ApplicationField> dataList = new ArrayList<>();
    HashMap<Integer, ApplicationField> dataHash = new HashMap<>();

    LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_application);
        rootView = findViewById(R.id.application_field_root);
        appId = getIntent().getIntExtra(AppData.APPLICATION_ID_KEY, 0);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(AppData.APPLICATION_TITLE_KEY));
        LoadApplicationFields loadApplicationFields = new LoadApplicationFields();
        loadApplicationFields.execute();
    }


    private void addEnumField(final ApplicationField applicationField) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout linearLayout = new LinearLayout(NewApplicationActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setId(applicationField.id);

        TextView titleTV = new TextView(this);
        titleTV.setText(applicationField.field_name);
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);

        linearLayout.addView(titleTV);
        linearLayout.addView(radioGroup);
        linearLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        rootView.addView(linearLayout);
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
        });
        // addLineSeperator();
    }


    private void addTextField(ApplicationField applicationField) {
        TextInputLayout editTextLayout = new TextInputLayout(this);
        editTextLayout.setId(applicationField.id);

        rootView.addView(editTextLayout);


        EditText editText = new EditText(this);
        editText.setHint(applicationField.field_name);
        // setEditTextAttributes(editText);
        editTextLayout.addView(editText);

        //addLineSeperator();
    }

    private void addFileField(final ApplicationField applicationField) {
        //RadioButtons are always added inside a RadioGroup
        LinearLayout linearLayout = new LinearLayout(NewApplicationActivity.this);
        linearLayout.setId(applicationField.id);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleTV = new TextView(this);
        titleTV.setText(applicationField.field_name);
        ImageView imageView = new ImageView(this);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
        imageView.setBackground(getResources().getDrawable(R.drawable.fram_primary));
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
        linearLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        rootView.addView(linearLayout);

        //addLineSeperator();
    }

//    private void addLineSeperator(int id) {
//        LinearLayout lineLayout = new LinearLayout(this);
//        lineLayout.setBackgroundColor(Color.GRAY);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                2);
//        params.setMargins(0, convertDpToPixel(10), 0, convertDpToPixel(10));
//        lineLayout.setLayoutParams(params);
//        rootView.addView(lineLayout);
//    }

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
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
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

    class LoadApplicationFields extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.getApplicationFieldData(NewApplicationActivity.this, AppData.authType + AppData.userToken, "" + appId);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dataList = GeneralUtility.parseApplicationField(s);
            for (ApplicationField applicationField : dataList) {
                dataHash.put(applicationField.id, applicationField);

                if (applicationField.field_type.matches("enum")) {
                    addEnumField(applicationField);
                } else if (applicationField.field_type.matches("text")) {
                    addTextField(applicationField);
                } else if (applicationField.field_type.matches("file")) {
                    addFileField(applicationField);
                }
            }

            for (ApplicationField applicationField : dataList) {
                if (applicationField.children != null) {
                    for (int i : applicationField.children) {
                        rootView.findViewById(i).setVisibility(View.GONE);
                    }
                }
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
//            base64Behind = GeneralUtility.getBase64(imageBitMap);
//            pickIdBehindPhoto.setImageBitmap(imageBitMap);
//            pickIdBehindPhoto.setColorFilter(null);
//            ImageViewCompat.setImageTintList(pickIdBehindPhoto, null);
            ((ImageView) (((LinearLayout) rootView.findViewById(requestCode)).getChildAt(1))).setImageBitmap(imageBitMap);

        }
    }
}
