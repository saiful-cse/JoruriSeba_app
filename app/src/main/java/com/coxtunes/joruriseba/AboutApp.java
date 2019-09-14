package com.coxtunes.joruriseba;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutApp extends AppCompatActivity {

    ImageView imageViewcall;
    TextView textView, textViewTermsbtn, textViewPolices;

    //http://creativesaif.com/api/joruri_seba_api/policies.php
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageViewcall = findViewById(R.id.dev_call);
        textView = findViewById(R.id.versionName);
        textViewTermsbtn = findViewById(R.id.terms_condition);

        PackageManager manager = this.getPackageManager();

        imageViewcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                callIntent.setData(Uri.parse("tel:+8801400559161"));
                startActivity(callIntent);
            }
        });

        try{

            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            textView.setText("Version: "+info.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        textViewTermsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://creativesaif.com/api/joruri_seba_api/policies.php"));
                startActivity(intent);
            }
        });

    }



}
