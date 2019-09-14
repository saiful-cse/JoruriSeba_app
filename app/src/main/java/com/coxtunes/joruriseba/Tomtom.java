package com.coxtunes.joruriseba;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tomtom extends AppCompatActivity {

    RecyclerView recyclerView;
    Contact_adapter contact_adapter;
    List<Contacts> contactsArrayList;
    Internet internet;
    SwipeRefreshLayout swipe;
    TextView textView;
    String next_page = "";
    boolean isLoading = true;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomtom);

        recyclerView = findViewById(R.id.recyclerView);
        contactsArrayList = new ArrayList<>();
        contact_adapter = new Contact_adapter(this,contactsArrayList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(contact_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        internet = new Internet(this);

        textView = findViewById(R.id.warning);

        progressBar = findViewById(R.id.progressBar);

        swipe = findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(internet.isConnected() && isLoading) {
                    contactLoad();
                    textView.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.VISIBLE);
                    swipe.setRefreshing(false);
                }

            }
        });

        if (internet.isConnected()){
            contactLoad();
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

    }

    /*
       contact load from server using volley
    */
    public void contactLoad(){
        isLoading = false;
        String url = getString(R.string.base_url)+"contacts/tomtom";
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

                        contacts.setName(jsonObject1.getString("name"));
                        contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                        contacts.setPhone(jsonObject1.getString("phone"));

                        contactsArrayList.add(contacts);
                    }
                    JSONObject jsonObject2 = jsonObject.getJSONObject("links");
                    next_page = jsonObject2.getString("next");
                    swipe.setRefreshing(false);
                    isLoading = true;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                contact_adapter.notifyDataSetChanged();

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

                        contacts.setName(jsonObject1.getString("name"));
                        contacts.setArea_chamber_shop(jsonObject1.getString("area"));
                        contacts.setPhone(jsonObject1.getString("phone"));

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
                contact_adapter.notifyDataSetChanged();

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
