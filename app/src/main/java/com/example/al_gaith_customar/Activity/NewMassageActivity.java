package com.example.al_gaith_customar.Activity;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.ApplicationData;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

public class NewMassageActivity extends AppCompatActivity {
    String massageType;
    private ProgressBar uploadPB;
    EditText massageET;
    SendMassageData sendMassageData = new SendMassageData();
    RadioGroup massageTypeRG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_massage);
        massageET = findViewById(R.id.massage_et);
        uploadPB = findViewById(R.id.upload_progress_layout);


        massageTypeRG = findViewById(R.id.massage_type_rg);
        massageTypeRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.indexOfChild(group.findViewById(checkedId));
                switch (index) {
                    case 0:
                        massageType = "suggestion";
                        break;
                    case 1:
                        massageType = "complain";
                        break;
                    case 2:
                        massageType = "inquiry";
                        break;
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void sendMassage(View view) {
        if (("" + massageET.getText()).isEmpty()) {

            Toast.makeText(this, "لا يمكن إرسال رسالة فارغة", Toast.LENGTH_SHORT).show();
        } else if (massageType == null) {
            Toast.makeText(this, "حدد نوع الرسالة من فضلك", Toast.LENGTH_SHORT).show();

        } else {
            sendMassageData.execute();
        }
    }


    class SendMassageData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uploadPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return GeneralUtility.sendMassageData(NewMassageActivity.this, AppData.authType + AppData.userToken, massageType, "" + massageET.getText());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            uploadPB.setVisibility(View.GONE);
            if (s != null) {
                Toast.makeText(NewMassageActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewMassageActivity.this, MassagesActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(NewMassageActivity.this, "حدث خطأ ما", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewMassageActivity.this, MassagesActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }
}
