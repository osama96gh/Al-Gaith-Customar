package com.example.al_gaith_customar.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.Announcement;
import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.Massage;
import com.example.al_gaith_customar.Data.Reply;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MassageDetailsActivity extends AppCompatActivity {
    Massage massage;
    ArrayList<Reply> dataList;
    TextView contentTV, typeTV, sendDateTV;
    ListAdapter customAdapter;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MassagesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage_details);
        contentTV = findViewById(R.id.item_content);
        typeTV = findViewById(R.id.item_type);
        sendDateTV = findViewById(R.id.item_number);

        ListView yourListView = (ListView) findViewById(R.id.reply_list_view);
        dataList = new ArrayList<>();
        Gson gson = new Gson();
        massage = gson.fromJson(getIntent().getStringExtra("massage"), Massage.class);

        contentTV.setText(massage.message);
        typeTV.setText(massage.getMassageType());
        sendDateTV.setText(massage.created_at);
        Reply reply = new Reply();
        reply.response = " رد تجريبي محلي";

        customAdapter = new ListAdapter(this, R.layout.row_reply, dataList);

        yourListView.setAdapter(customAdapter);

        LoadMassageReply loadMassageReply = new LoadMassageReply();
        loadMassageReply.execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public class ListAdapter extends ArrayAdapter<Reply> {

        private int resourceLayout;
        private Context mContext;

        public ListAdapter(Context context, int resource, List<Reply> items) {
            super(context, resource, items);
            this.resourceLayout = resource;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                v = vi.inflate(resourceLayout, null);
            }

            Reply p = getItem(position);

            if (p != null) {
                TextView tt1 = (TextView) v.findViewById(R.id.reply_text);
                TextView date = v.findViewById(R.id.reply_date);

                if (tt1 != null) {
                    tt1.setText(p.response);
                }
                if (date != null) {
                    date.setText(p.created_at);
                }


            }

            return v;
        }

    }

    class LoadMassageReply extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            return GeneralUtility.getMassageData(MassageDetailsActivity.this, AppData.authType + AppData.userToken, "" + massage.id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for (Reply reply : GeneralUtility.Reply(s)) {
                dataList.add(reply);
            }

            notifyDataChange();
        }

        private void notifyDataChange() {
            customAdapter.notifyDataSetChanged();
        }
    }
}
