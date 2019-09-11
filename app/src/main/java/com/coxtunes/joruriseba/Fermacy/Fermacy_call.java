package com.coxtunes.joruriseba.Fermacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class Fermacy_call extends Fragment{

    RecyclerView recyclerView;
    Contact_adapter contact_adapter;
    List<Contacts> contactsArrayList;
    ProgressBar progressBar;
    Internet internet;
    SwipeRefreshLayout swipe;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fermacy_call, container, false);

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

        return rootView;
    }


    /*
        News load from server using volley
     */
    public void contactLoad(){
        String serverUrl = getString(R.string.server_url)+"contacts/read.php?table=pharmacy";
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("serverRsp",response);
                try {
                    contactsArrayList.clear();
                    JSONArray jsonArray= new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        Contacts contacts = new Contacts();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        contacts.setName(jsonObject.getString("name"));
                        contacts.setArea_chamber_shop(jsonObject.getString("shop"));
                        contacts.setPhone(jsonObject.getString("phone"));

                        contactsArrayList.add(contacts);
                        swipe.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                contact_adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("serverRsp",error.toString());
            }
        });
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }
}
