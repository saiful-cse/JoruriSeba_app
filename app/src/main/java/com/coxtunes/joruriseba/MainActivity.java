package com.coxtunes.joruriseba;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;

import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import com.coxtunes.joruriseba.Cng.Cng;
import com.coxtunes.joruriseba.Doctor.Doctor;
import com.coxtunes.joruriseba.Electrician.Elec;
import com.coxtunes.joruriseba.Fermacy.Fermacy;
import com.coxtunes.joruriseba.Fire_service.Fire_service;
import com.coxtunes.joruriseba.Gas.Gas;
import com.coxtunes.joruriseba.Log_in.Log_in;
import com.coxtunes.joruriseba.Polly_biddut.Polly_biddut;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CardView cardWifi, cardTomtom, cardDoc, cardFer, cardCn, cardGa,
            cardElec, cardPoll, cardFirs, cardAboutapp;

    /*
    network connection detector warning.
     */
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private boolean internetConnected=true;
    //------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_page_toolbar); //main activity toolbar
        setSupportActionBar(toolbar);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.content_main); //network connection detector warning.

        //-------card click------------

        cardWifi = findViewById(R.id.cardWifi);
        cardWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Wifi.class);
                startActivity(intent);
            }
        });

        cardTomtom = findViewById(R.id.cardTomtom);
        cardTomtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Tomtom.class);
                startActivity(intent);
            }
        });

        cardDoc = (CardView) findViewById(R.id.cardDoctor);
        cardDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Doctor.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        cardFer = (CardView) findViewById(R.id.cardFermacy);
        cardFer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Fermacy.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        cardCn = (CardView) findViewById(R.id.cardCng);
        cardCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cng.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        cardGa = (CardView) findViewById(R.id.cardGas);
        cardGa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Gas.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        cardElec = (CardView) findViewById(R.id.cardElectrician);
        cardElec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Elec.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        cardPoll = (CardView) findViewById(R.id.cardPolly);
        cardPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Polly_biddut.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        cardFirs = (CardView) findViewById(R.id.cardFirService);
        cardFirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Fire_service.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);

            }
        });
        cardAboutapp = (CardView) findViewById(R.id.cardAboutApp);
        cardAboutapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutApp.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            }
        });
        //---------------------------------------


        //------showing for navigaton drawer-----
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout); //
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null); //for orginal nav menu icon color
        //-----------------------------------------------------------------------------
    }

    //------When click back, then navigation Drawer close-----
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.right_out,R.anim.left_in);
        }

    }

    //main page toolbar menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //Toolbar menu click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutApp.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            return true;
        }

        if (id == R.id.action_login) {
            Intent intent = new Intent(MainActivity.this, Log_in.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    //Drawer menu item click
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wifi) {
            Intent intent = new Intent(MainActivity.this, Doctor.class);
            startActivity(intent);


        } else if (id == R.id.nav_tomtom) {
            Intent intent = new Intent(MainActivity.this, Tomtom.class);
            startActivity(intent);


        } else if (id == R.id.nav_doctor) {
            Intent intent = new Intent(MainActivity.this, Doctor.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);

        } else if (id == R.id.nav_fermacy) {
            Intent intent = new Intent(MainActivity.this, Fermacy.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);

        } else if (id == R.id.nav_cng) {
            Intent intent = new Intent(MainActivity.this, Cng.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);

        } else if (id == R.id.nav_gas) {
            Intent intent = new Intent(MainActivity.this, Gas.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);

        } else if (id == R.id.nav_electrician) {
            Intent intent = new Intent(MainActivity.this, Elec.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        }else if (id == R.id.nav_polly_biddut) {
            Intent intent = new Intent(MainActivity.this, Polly_biddut.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        }else if (id == R.id.nav_fir_service){
            Intent intent = new Intent(MainActivity.this, Fire_service.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);

        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutApp.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);


        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, Log_in.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }


        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
    network connection detector warning.
     */

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    /**
     *  Method to register runtime broadcast receiver to show snackbar alert for internet connection..
     */
    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     *  Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status,false);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
    private void setSnackbarMessage(String status,boolean showBar) {
        String internetStatus="";
        if(status.equalsIgnoreCase("Wifi enabled")||status.equalsIgnoreCase("Mobile data enabled")){
            internetStatus="ইন্টারনেট কানেক্ট হয়েছে";
        }else {
            internetStatus="ইন্টারনেট কানেকশন নাই";
        }
        snackbar = Snackbar
                .make(coordinatorLayout, internetStatus, Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        // Changing message text color
        snackbar.setActionTextColor(Color.WHITE);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);

        if(internetStatus.equalsIgnoreCase("ইন্টারনেট কানেকশন নাই")){
            if(internetConnected){
                snackbar.show();
                internetConnected=false;
            }
        }else{
            if(!internetConnected){
                internetConnected=true;
                snackbar.show();
            }
        }
    }
    //-------------------------------------------------------------------

}
