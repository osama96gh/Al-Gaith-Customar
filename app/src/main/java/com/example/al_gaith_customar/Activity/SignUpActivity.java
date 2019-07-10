package com.example.al_gaith_customar.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

public class SignUpActivity extends AppCompatActivity {
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
    boolean containProfilImage = false;
    ImageInfo profileImageInfo;


    private UserSignUpTask mSignUpTask = null;

    public static String USER_ID = null;

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
                case GALLERY_REQUEST_CODE_PERSONAL:

                    imageUri = data.getData();
                    imageBitMap = GeneralUtility.getImageFromUri(SignUpActivity.this, imageUri);

                    try {
                        File outputDir = this.getCacheDir(); // context being the Activity pointer

                        File outputFile = File.createTempFile("osaka", ".jpg", outputDir);
                        OutputStream outStream = null;

                        outStream = new FileOutputStream(outputFile);
                        imageBitMap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
                        outStream.close();
                        profileImageInfo = new ImageInfo(outputFile.getAbsolutePath(), "image-profile");
                        // uploadImage("" + requestCode);
                        pickPersonalPhoto.setImageBitmap(imageBitMap);
                        containProfilImage = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    break;
            }
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
                    phon_et.getText().toString());
            return response;
        }

        @Override
        protected void onPostExecute(final String response) {

            mProgressLayout.setVisibility(View.GONE);

            if (response.matches("no error")) {
//                Intent intent = new Intent(SignUpActivity.this, SplachActivity.class);
//                startActivity(intent);
//                finish();
            if (USER_ID != null&&!USER_ID.isEmpty() && containProfilImage) {
                    try {
                        uploadProfileImage(USER_ID);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Intent intent = new Intent(SignUpActivity.this, SplachActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                mErrorLayout.setVisibility(View.VISIBLE);
                mErrorContaner.setText(response);
            }
        }

        @Override
        protected void onCancelled() {
         }
    }

    void uploadProfileImage(String user_id) throws MalformedURLException, FileNotFoundException {
        MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(SignUpActivity.this, AppData.BASIC_URI + "/upload/files");
        multipartUploadRequest.addFileToUpload(profileImageInfo.path, profileImageInfo.getKey());


        String uploadId = multipartUploadRequest
                .setNotificationConfig(new UploadNotificationConfig())
                .setMaxRetries(2)
                .addParameter("user_id", user_id)
                .addParameter("type", "profile")
                .setDelegate(new UploadServiceBroadcastReceiver() {
                    @Override
                    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                        Log.println(Log.ASSERT, "resulte", serverResponse.getBodyAsString());
                        super.onCompleted(context, uploadInfo, serverResponse);
                        Log.println(Log.ASSERT, "upload profile result", serverResponse.getBodyAsString());
                        String imge_url = GeneralUtility.getSuccessMassage(serverResponse.getBodyAsString());
                        GeneralUtility.saveString(AppData.USER_PHOTO_KEY, imge_url, context);

                        Intent intent = new Intent(SignUpActivity.this, SplachActivity.class);
                        startActivity(intent);
                        finish();
                        //onBackPressed();
                    }

                    @Override
                    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                        mErrorLayout.setVisibility(View.VISIBLE);
                        mErrorContaner.setText("خطأ في رفع صورة المستخدم");
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

}
