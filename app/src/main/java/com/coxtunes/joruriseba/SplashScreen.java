package com.coxtunes.joruriseba.SplashScreen;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MainActivity;
import com.coxtunes.joruriseba.R;
import com.coxtunes.joruriseba.User_Login_Reg.GettingNumber;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {


    TextView t1;
    SharedPreferences sp;
    Internet internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide(); //hide action bar

        internet = new Internet(this);

        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
        startActivity(intent);

//        if (!internet.isConnected()){
//            connError();
//        }else{
//            sp = getSharedPreferences("registration",MODE_PRIVATE);
//            t1 = findViewById(R.id.headline);
//            Typeface tp1 = Typeface.createFromAsset(getAssets(), "soliman_lipi.ttf");
//            t1.setTypeface(tp1);
//            new Handler().postDelayed(new Runnable() {
//                //showing splash screen with a timer.
//                @Override
//                public void run() {
//                    if(sp.getBoolean("registered",false)){
//                        // start main activity after time.
//                        Intent intent = new Intent(SplashScreen.this,MainActivity.class);
//                        startActivity(intent);
//                        //close activity
//                        finish();
//                        overridePendingTransition(R.anim.right_in,R.anim.left_out);
//                    }else{
//                        Intent intent = new Intent(SplashScreen.this,GettingNumber.class);
//                        startActivity(intent);
//                    }
//
//                }
//            },1000);
//        }

    }


    public boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Service.CONNECTIVITY_SERVICE);

        if(connectivity != null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if(info != null){

                if(info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }

            }
        }
        return false;
    }

    public void connError(){
        AlertDialog.Builder aleart1 = new AlertDialog.Builder(this);
        aleart1.setCancelable(false);
        aleart1.setTitle("দুঃখিত!!");
        aleart1.setMessage("এই অ্যাপটিতে ইন্টারনেট কানেকশন ছাড়া প্রবেশ করা যাবেনা। দয়া করে ইন্টারনেট কানেকশন অন করুন");
        aleart1.setIcon(R.drawable.sorry_icon);

        aleart1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dlg = aleart1.create();
        dlg.show();
    }
}
