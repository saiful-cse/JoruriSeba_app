package com.coxtunes.joruriseba.Polly_biddut;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coxtunes.joruriseba.Contact_adapter;
import com.coxtunes.joruriseba.Contacts;
import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saif on 5/3/2017.
 */

public class Pollly_biddut_call extends Fragment{

    RecyclerView recyclerView;
    Contact_adapter contact_adapter;
    List<Contacts> contactsArrayList;
    ProgressBar progressBar;
    Internet internet;
    SwipeRefreshLayout swipe;
    TextView textView;
    String next_page = "";
    boolean isLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.polly_biddut_call, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        contactsArrayList = new ArrayList<>();
        contact_adapter = new Contact_adapter(getContext(),contactsArrayList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(contact_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar = rootView.findViewById(R.id.progressBar);
        internet = new Internet(getContext());

        textView = rootView.findViewById(R.id.warning);

        swipe = rootView.findViewById(R.id.swipe_refresh_layout);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(internet.isConnected()) {
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
                            Toast.makeText(getContext(),"No more contact",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        return rootView;
    }


    /*
        contact load from server using volley
     */
    public void contactLoad(){
        isLoading = false;
        String url = getString(R.string.base_url)+"contacts/polly";
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
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(),error.toString(),Toast.LENGTH_SHORT).show();
                isLoading = true;
            }
        });
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }
}
