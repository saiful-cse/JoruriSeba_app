package com.coxtunes.joruriseba;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coxtunes.joruriseba.Cng.Cng;
import com.coxtunes.joruriseba.Doctor.Doctor;
import com.coxtunes.joruriseba.Electrician.Elec;
import com.coxtunes.joruriseba.Fermacy.Fermacy;
import com.coxtunes.joruriseba.Fire_service.Fire_service;
import com.coxtunes.joruriseba.Gas.Gas;
import com.coxtunes.joruriseba.Log_in.Log_in;
import com.coxtunes.joruriseba.NewsPostUpdateRead.News;
import com.coxtunes.joruriseba.NewsPostUpdateRead.NewsAdapter;
import com.coxtunes.joruriseba.Polly_biddut.Polly_biddut;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    InterstitialAd mInterstitialAdDoctor,mInterstitialAdFermacy,mInterstitialAdCng,
    mInterstitialAdGas,mInterstitialAdElec,mInterstitialAdPolly,mInterstitialAdFirs;

    TextView tv_fazar,tv_zohar,tv_asar,tv_magrib,tv_isha,tv_joma;

    SwipeRefreshLayout swipe;
    Internet internet; //internet "class" call

    LinearLayout aleart_layout1, aleart_layout2, layout3; //layout visible or invisible

    CardView cardDoc, cardFer, cardCn, cardGa, cardElec, cardPoll, cardFirs, cardAboutapp;

    ProgressBar progressBar;

    /*
        News
     */
    RecyclerView recyclerView;

    NewsAdapter newsAdapter;
    List<News> newsArrayList;



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

    /*
    Runtime permission
     */
    public int[] permissionCheck;
    public int MY_PERMISSIONS_ACCESS_ALL_PERMISSION;

    public String[] permissions = new String[]
            {android.Manifest.permission.CALL_PHONE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_page_toolbar); //main activity toolbar
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        // Runtime permission check
        permissionCheck = new int[]
                {
                        ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.CALL_PHONE)
                };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestForPermission();

        }


        internet = new Internet(this);
        aleart_layout1 = (LinearLayout)findViewById(R.id.internet_aleart1);
        aleart_layout2 = (LinearLayout)findViewById(R.id.internet_aleart2);
        layout3 = (LinearLayout)findViewById(R.id.namaj_types);

        /*
            News component initialize
         */
        newsArrayList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        newsAdapter  = new NewsAdapter(MainActivity.this, newsArrayList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if(internet.isConnected()){
            layout3.setVisibility(View.VISIBLE);
            aleart_layout1.setVisibility(View.GONE);
            aleart_layout2.setVisibility(View.GONE);
            newsLoad();

        }else{
            layout3.setVisibility(View.GONE);
            aleart_layout1.setVisibility(View.VISIBLE);
            aleart_layout2.setVisibility(View.VISIBLE);
        }

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.content_main); //network connection detector warning.

        tv_fazar = (TextView)findViewById(R.id.fazar);
        tv_zohar = (TextView)findViewById(R.id.johor);
        tv_asar = (TextView)findViewById(R.id.asor);
        tv_magrib = (TextView)findViewById(R.id.magrib);
        tv_isha = (TextView)findViewById(R.id.isha);
        tv_joma = (TextView)findViewById(R.id.joma);

        //----btn click ad show------
        mInterstitialAdDoctor = new InterstitialAd(this);
        mInterstitialAdDoctor.setAdUnitId(getString(R.string.doctor_insts));
        AdRequest int_adRequestDoctor = new AdRequest.Builder().build();
        mInterstitialAdDoctor.loadAd(int_adRequestDoctor);

        mInterstitialAdFermacy = new InterstitialAd(this);
        mInterstitialAdFermacy.setAdUnitId(getString(R.string.fermacy_intst));
        AdRequest int_adRequestFermacy = new AdRequest.Builder().build();
        mInterstitialAdFermacy.loadAd(int_adRequestFermacy);

        mInterstitialAdCng = new InterstitialAd(this);
        mInterstitialAdCng.setAdUnitId(getString(R.string.cng_intst));
        AdRequest int_adRequestCng = new AdRequest.Builder().build();
        mInterstitialAdCng.loadAd(int_adRequestCng);

        mInterstitialAdGas = new InterstitialAd(this);
        mInterstitialAdGas.setAdUnitId(getString(R.string.gas_intst));
        AdRequest int_adRequestGas = new AdRequest.Builder().build();
        mInterstitialAdGas.loadAd(int_adRequestGas);

        mInterstitialAdElec = new InterstitialAd(this);
        mInterstitialAdElec.setAdUnitId(getString(R.string.elec_intst));
        AdRequest int_adRequestElec = new AdRequest.Builder().build();
        mInterstitialAdElec.loadAd(int_adRequestElec);

        mInterstitialAdPolly = new InterstitialAd(this);
        mInterstitialAdPolly.setAdUnitId(getString(R.string.polly_intst));
        AdRequest int_adRequestPolly = new AdRequest.Builder().build();
        mInterstitialAdPolly.loadAd(int_adRequestPolly);

        mInterstitialAdFirs = new InterstitialAd(this);
        mInterstitialAdFirs.setAdUnitId(getString(R.string.fsb_intst));
        AdRequest int_adRequestFirs = new AdRequest.Builder().build();
        mInterstitialAdFirs.loadAd(int_adRequestFirs);

        //-------card click------------

        cardDoc = (CardView) findViewById(R.id.cardDoctor);
        cardDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Doctor.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdDoctor.isLoaded()) {
                    mInterstitialAdDoctor.show();
                }
            }
        });
        cardFer = (CardView) findViewById(R.id.cardFermacy);
        cardFer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Fermacy.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdFermacy.isLoaded()) {
                    mInterstitialAdFermacy.show();
                }
            }
        });
        cardCn = (CardView) findViewById(R.id.cardCng);
        cardCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cng.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdCng.isLoaded()) {
                    mInterstitialAdCng.show();
                }
            }
        });
        cardGa = (CardView) findViewById(R.id.cardGas);
        cardGa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Gas.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdGas.isLoaded()) {
                    mInterstitialAdGas.show();
                }
            }
        });
        cardElec = (CardView) findViewById(R.id.cardElectrician);
        cardElec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Elec.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdElec.isLoaded()) {
                    mInterstitialAdElec.show();
                }
            }
        });
        cardPoll = (CardView) findViewById(R.id.cardPolly);
        cardPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Polly_biddut.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdPolly.isLoaded()) {
                    mInterstitialAdPolly.show();
                }
            }
        });
        cardFirs = (CardView) findViewById(R.id.cardFirService);
        cardFirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Fire_service.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);
                if (mInterstitialAdFirs.isLoaded()) {
                    mInterstitialAdFirs.show();
                }

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
        //------------------------------------------------------------------------------

        //-----------namaz time show------------
        DatabaseReference namaz_schedule_reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://joruri-seba.firebaseio.com/Namaz_Schedule");
        namaz_schedule_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fazar = dataSnapshot.child("fazar").getValue(String.class);
                String zohar = dataSnapshot.child("zohar").getValue(String.class);
                String asar = dataSnapshot.child("asar").getValue(String.class);
                String magrib = dataSnapshot.child("magrib").getValue(String.class);
                String isha = dataSnapshot.child("isha").getValue(String.class);
                String joma = dataSnapshot.child("joma").getValue(String.class);

                tv_fazar.setText(fazar);
                tv_zohar.setText(zohar);
                tv_asar.setText(asar);
                tv_magrib.setText(magrib);
                tv_isha.setText(isha);
                tv_joma.setText(joma);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplication(),"Server Error!",Toast.LENGTH_SHORT).show();
            }
        });
        //---------------------------------------------------------------

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(internet.isConnected()){
                    newsLoad();
                }else{
                    Toast.makeText(getApplication(),"Check internet connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    /*
        News load from server using volley
     */
    public void newsLoad(){

        progressBar.setVisibility(View.VISIBLE);
        String serverUrl = getString(R.string.server_url)+"News_read.php";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    newsArrayList.clear();
                    JSONArray jsonArray= new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        News news = new News();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        news.setNewsPhoto(jsonObject.getString("photo"));
                        news.setNewsTitle(jsonObject.getString("news_title"));
                        news.setNewsTime(jsonObject.getString("date_time"));
                        news.setNewsDesc(jsonObject.getString("news_desc"));
                        news.setNewsEditor(jsonObject.getString("editor"));

                        newsArrayList.add(news);
                        progressBar.setVisibility(View.GONE);
                        swipe.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                newsAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }


    // if SDK > 23
    public void requestForPermission() {

        if (permissionCheck[0] != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_ACCESS_ALL_PERMISSION);
        }
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

        if (id == R.id.action_developer) {
            Intent intent = new Intent(MainActivity.this, Developer.class);
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

        if (id == R.id.nav_doctor) {
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


        } else if (id == R.id.nav_developer) {
            Intent intent = new Intent(MainActivity.this, Developer.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in,R.anim.left_out);

        }else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, Log_in.class);
            startActivity(intent);
            MainActivity.this.overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
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
