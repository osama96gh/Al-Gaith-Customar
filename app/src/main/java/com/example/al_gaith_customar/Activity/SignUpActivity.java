package com.example.al_gaith_customar.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

public class SignUpActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE_ID_FRONT = 11;
    private static final int GALLERY_REQUEST_CODE_ID_BEHIND = 22;
    private static final int GALLERY_REQUEST_CODE_PERSONAL = 33;
    EditText firstName_et;
    EditText lastName_et;
    EditText username_et;
    EditText password_et;
    EditText password_conf_et;
    EditText mobile_et;
    EditText phon_et;

    private View mErrorLayout;
    private View mProgressLayout;
    private TextView mErrorContaner;

    ImageButton pickPersonalPhoto;
    String base64Personal="";
    ImageButton pickIdFrontPhoto;
    String base64Front="";
    ImageButton pickIdBehindPhoto;
    String base64Behind="";

    private UserSignUpTask mSignUpTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstName_et = findViewById(R.id.sign_up_first_name);
        lastName_et = findViewById(R.id.sign_up_last_name);

        username_et = findViewById(R.id.sign_up_username);
        password_et = findViewById(R.id.sign_up_password);
        password_conf_et = findViewById(R.id.sign_up_password_conf);
        mobile_et = findViewById(R.id.sign_up_mobile);
        phon_et = findViewById(R.id.sign_up_phone);

        pickIdFrontPhoto = findViewById(R.id.sign_up_id_image_front);
        pickIdBehindPhoto = findViewById(R.id.sign_up_id_image_behind);
        pickPersonalPhoto = findViewById(R.id.sign_up_personal_image);

        mErrorContaner = findViewById(R.id.error_tv);
        mErrorLayout = findViewById(R.id.error_ll);
        mProgressLayout = findViewById(R.id.progress_layout);
        mErrorLayout.setVisibility(View.GONE);

    }

    public void pickPersonalPhoto(View view) {
        pickImage(GALLERY_REQUEST_CODE_PERSONAL);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        Uri imageUri;
        Bitmap imageBitMap;

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE_ID_BEHIND:
                    //data.getData returns the content URI for the selected Image
                    imageUri = data.getData();
                    imageBitMap = GeneralUtility.getImageFromUri(SignUpActivity.this, imageUri);
                    base64Behind = GeneralUtility.getBase64(imageBitMap);
                    pickIdBehindPhoto.setImageBitmap(imageBitMap);
                    pickIdBehindPhoto.setColorFilter(null);
                    ImageViewCompat.setImageTintList(pickIdBehindPhoto, null);

                    break;
                case GALLERY_REQUEST_CODE_ID_FRONT:
                    //data.getData returns the content URI for the selected Image
                    imageUri = data.getData();
                    imageBitMap = GeneralUtility.getImageFromUri(SignUpActivity.this, imageUri);
                    base64Front = GeneralUtility.getBase64(imageBitMap);

                    ImageViewCompat.setImageTintList(pickIdFrontPhoto, null);

                    pickIdFrontPhoto.setImageBitmap(imageBitMap);
                    pickIdFrontPhoto.setColorFilter(null);
                    break;
                case GALLERY_REQUEST_CODE_PERSONAL:
                    //data.getData return the content URI for the selected Image

                    imageUri = data.getData();
                    imageBitMap = GeneralUtility.getImageFromUri(SignUpActivity.this, imageUri);
                    base64Personal = GeneralUtility.getBase64(imageBitMap);
                    pickPersonalPhoto.setColorFilter(null);
                    ImageViewCompat.setImageTintList(pickPersonalPhoto, null);

                    pickPersonalPhoto.setImageBitmap(imageBitMap);

                    break;
            }
    }

    public void pickIdFrontPhoto(View view) {
        pickImage(GALLERY_REQUEST_CODE_ID_FRONT);

    }

    public void pickIdBehindPhoto(View view) {
        pickImage(GALLERY_REQUEST_CODE_ID_BEHIND);

    }

    void requstReadPermission() {
        ActivityCompat.requestPermissions(SignUpActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
                    Toast.makeText(SignUpActivity.this, "فشل التطبيق في الحصول على إذن القراءة", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void sineUp(View view) {
        mSignUpTask = new UserSignUpTask();
        mSignUpTask.execute();
    }

    public void goLogIn(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public class UserSignUpTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            mErrorLayout.setVisibility(View.GONE);
            mProgressLayout.setVisibility(View.VISIBLE);
            super.onPreExecute();
            Log.println(Log.ASSERT, "id photo", base64Behind + " " + base64Front);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // TODO: register the new account here.
            String response = GeneralUtility.signUp(
                    SignUpActivity.this,
                    firstName_et.getText().toString(),
                    lastName_et.getText().toString(),
                    username_et.getText().toString(),
                    password_et.getText().toString(),
                    password_conf_et.getText().toString(),
                    mobile_et.getText().toString(),
                    phon_et.getText().toString(),
                    base64Personal,
                    base64Front,
                    base64Behind
            );
            return response;
        }

        @Override
        protected void onPostExecute(final String response) {

            mProgressLayout.setVisibility(View.GONE);

            if (response.matches("no error")) {

                    Intent intent = new Intent(SignUpActivity.this, SplachActivity.class);
                    startActivity(intent);
                    finish();

            } else {
                mErrorLayout.setVisibility(View.VISIBLE);
                mErrorContaner.setText(response);
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(SignUpActivity.this, "onCancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
