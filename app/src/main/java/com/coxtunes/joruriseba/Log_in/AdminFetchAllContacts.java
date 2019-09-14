package com.coxtunes.joruriseba.Log_in;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coxtunes.joruriseba.Contacts;
import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminFetchAllContacts extends AppCompatActivity {


    RecyclerView recyclerView;
    AdminContact_adapter adminContact_adapter;
    List<Contacts> contactsArrayList;
    Internet internet;
    SwipeRefreshLayout swipe;
    TextView textView;
    String next_page = "";
    boolean isLoading = true;
    ProgressBar progressBar;

    String service_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fetch_all_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //action bar back icon

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        service_name = getIntent().getStringExtra("service_name");
        recyclerView = findViewById(R.id.recyclerView);
        contactsArrayList = new ArrayList<>();
        adminContact_adapter = new AdminContact_adapter(getApplicationContext(),contactsArrayList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adminContact_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        internet = new Internet(getApplicationContext());

        textView = findViewById(R.id.warning);

        progressBar = findViewById(R.id.progressBar);

        swipe = findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(internet.isConnected()) {
                    contactLoad(service_name);
                    textView.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.VISIBLE);
                    swipe.setRefreshing(false);
                }

            }
        });

        if (internet.isConnected()){
            contactLoad(service_name);
        }else{
            textView.setVisibility(View.VISIBLE);
        }

        // here add a recyclerView listener, to listen to scrolling,
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            //this is the ONLY method that we need, ignore the rest
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // Recycle view scrolling downwards...
                    // this if statement detects when user reaches the end of recyclerView, this is only time we should load more
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        // remember "!" is the same as "== false"
                        // here we are now allowed to load more, but we need to be careful
                        // we must check if itShouldLoadMore variable is true [unlocked]
                        if (isLoading && !next_page.equals("null")) {
                            more_contactLoad(next_page);

                        }else if(next_page.equals("null")){
                            Toast.makeText(getApplicationContext(),"No more contact",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminFetchAllContacts.this, AdminContactsAdd.class);
                intent.putExtra("service_name", service_name);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void contactLoad(final String service_name){
        isLoading = false;
        String url = getString(R.string.base_url)+"contacts/"+service_name;
        swipe.setRefreshing(true);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    contactsArrayList.clear();
                    //Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){

                        Contacts contacts = new Contacts();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);


                        if (service_name.equals("doctor")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("chamber"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("doctor");

                        }else if(service_name.equals("gas") ){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("shop"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("gas");

                        }else if(service_name.equals("pharmacy")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("shop"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("pharmacy");

                        } else if(service_name.equals("tomtom")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("tomtom");

                        } else if(service_name.equals("cng")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("cng");

                        }else if(service_name.equals("elec")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("elec");
                        }

                        contactsArrayList.add(contacts);
                    }
                    JSONObject jsonObject2 = jsonObject.getJSONObject("links");
                    next_page = jsonObject2.getString("next");
                    swipe.setRefreshing(false);
                    isLoading = true;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adminContact_adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipe.setRefreshing(false);
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                isLoading = true;
            }
        });
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void more_contactLoad(String url){
        isLoading = false;
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Toast.makeText(getContext(),response,Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    for (int i=0;i<jsonArray.length();i++){

                        Contacts contacts = new Contacts();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        if (service_name.equals("doctor")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("chamber"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("doctor");

                        }else if(service_name.equals("gas") ){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("shop"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("gas");

                        }else if(service_name.equals("pharmacy")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("shop"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("pharmacy");

                        } else if(service_name.equals("tomtom")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("tomtom");

                        } else if(service_name.equals("cng")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("cng");

                        }else if(service_name.equals("elec")){
                            contacts.setId(jsonObject1.getString("id"));
                            contacts.setName(jsonObject1.getString("name"));
                            contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                            contacts.setPhone(jsonObject1.getString("phone"));
                            contacts.setService_name("elec");
                        }

                        contactsArrayList.add(contacts);
                    }
                    JSONObject jsonObject2 = jsonObject.getJSONObject("links");
                    next_page = jsonObject2.getString("next");
                    progressBar.setVisibility(View.GONE);
                    isLoading = true;

                    //Toast.makeText(getContext(),next_page,Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adminContact_adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
                isLoading = true;
            }
        });
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }
}

