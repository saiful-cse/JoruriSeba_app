package com.coxtunes.joruriseba;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.net.ConnectivityManager;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coxtunes.joruriseba.UserRegistration.RegStep1;
import com.crashlytics.android.Crashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {


    TextView t1;

    SharedPreferences pref;
    String email;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide(); //hide action bar


        t1 = findViewById(R.id.headline);
        Typeface tp1 = Typeface.createFromAsset(getAssets(), "soliman_lipi.ttf");
        t1.setTypeface(tp1);

        pref = getApplicationContext().getSharedPreferences("reg", MODE_PRIVATE);
        email = pref.getString("email", null);
        progressBar = findViewById(R.id.progressSplash);

        if (isNetworkConnected() ){
            //calling a function for Load post
            user_check();

        }else{
            aleartDialog("এই অ্যাপটিতে ইন্টারনেট কানেকশন ছাড়া প্রবেশ করা যাবেনা। দয়া করে ইন্টারনেট কানেকশন অন করুন");
            //Snackbar.make(findViewById(android.R.id.content),"Please!! Check internet connection.",Snackbar.LENGTH_LONG).show();

        }
    }


    //get profile info
    public void user_check(){

        progressBar.setVisibility(View.VISIBLE);
        //String url = getString(R.string.base_url)+"user/"+email;
        String url = "https://creativesaif.com/api/joruri_seba_api/api/user/saif@gmail.com";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    if (message.equals("200")){
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));

                    }else if (message.equals("404")){

                        startActivity(new Intent(SplashScreen.this, RegStep1.class));
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SplashScreen.this, e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressBar.setVisibility(View.GONE);
                //aleartDialog("Something went wrong!! Try again later.");
                aleartDialog(volleyError.toString());
                //Toast.makeText(SplashScreen.this, volleyError.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance().addToRequestQueue(request);
    }


    public void aleartDialog(String message){
        AlertDialog.Builder aleart1 = new AlertDialog.Builder(this);
        aleart1.setCancelable(false);
        aleart1.setTitle("দুঃখিত!!");
        aleart1.setMessage(message);
        aleart1.setIcon(R.drawable.sorry_icon);

        aleart1.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dlg = aleart1.create();
        dlg.show();
    }


    //Internet connection check
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
