package com.coxtunes.joruriseba.Log_in;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coxtunes.joruriseba.Contacts;
import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.ProgressDialog;
import com.coxtunes.joruriseba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminContactsUpdateDelete extends AppCompatActivity {

    String id, name, phone, area_chamber_shop;

    String finalname, finalphone, finalarea_chamber_shop;

    TextView demoName, demoService, demoPhne;
    EditText edname, edarea_shop_chamber, edphone;
    Button update, delete;

    Internet internet;

    ProgressDialog progressDialog;

    String service_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contacts_update_delete);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        internet = new Internet(this);
        demoName = findViewById(R.id.texName);
        demoService = findViewById(R.id.texService);
        demoPhne = findViewById(R.id.texPhone);

        edname = findViewById(R.id.edPersionName);
        edarea_shop_chamber = findViewById(R.id.edAreaChamberShop);
        edphone = findViewById(R.id.edPh);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        area_chamber_shop = getIntent().getStringExtra("area_chamber_shop");
        phone = getIntent().getStringExtra("phone");

        service_name = getIntent().getStringExtra("service_name");
        progressDialog = new ProgressDialog(this);


        //set content on edittext filed
        edname.setText(name);
        edarea_shop_chamber.setText(area_chamber_shop);
        edphone.setText(phone);

        switch (service_name) {

            case "doctor":
                demoName.setText("ডাক্তারের নাম");
                demoService.setText("চেম্বারের নাম");
                demoPhne.setText("ডাক্তারের ফোন নাম্বার");
                break;

            case "pharmacy":
                demoService.setText("দোকানের নাম");
                demoName.setText("প্রোপাইটরের নাম");
                demoPhne.setText("ফোন নাম্বার");
                break;

            case "gas":
                demoService.setText("দোকানের নাম");
                demoName.setText("প্রোপাইটরের নাম");
                demoPhne.setText("ফোন নাম্বার");
                break;

            case "cng":
                demoService.setText("এলাকার নাম");
                demoName.setText("ড্রাইভারের নাম");
                demoPhne.setText("ফোন নাম্বার");
                break;

            case "elec":
                demoService.setText("এলাকার নাম");
                demoName.setText("ইলেকট্রিশিয়ানের নাম");
                demoPhne.setText("ফোন নাম্বার");
                break;

            case "tomtom":
                demoService.setText("এলাকার নাম");
                demoName.setText("ড্রাইভারের নাম");
                demoPhne.setText("ফোন নাম্বার");
                break;

        }


        update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalname = edname.getText().toString().trim();
                finalarea_chamber_shop = edarea_shop_chamber.getText().toString().trim();
                finalphone = edphone.getText().toString();

                if (!internet.isConnected()){
                    Toast.makeText(getApplicationContext(), "ইন্টারনেট কানেকশন অন করুন", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty() || area_chamber_shop.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "খালিঘর পুরন করুন", Toast.LENGTH_SHORT).show();
                }else {
                    contacts_update(service_name, id);
                }
            }
        });

        delete = findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_delete(service_name, id);
            }
        });
    }

    public void contacts_update(String service, String idd)
    {
        progressDialog.showDialog();
        String url = getString(R.string.base_url)+"contact/"+service+"/"+idd;

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();

                progressDialog.hideDialog();

                try{

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    if (message.equals("200")){

                        finish();
                        Toast.makeText(AdminContactsUpdateDelete.this, "ধন্যবাদ, তথ্য সংশোধনের জন্য",Toast.LENGTH_LONG).show();

                    }else{

                        Toast.makeText(AdminContactsUpdateDelete.this, message,Toast.LENGTH_LONG).show();

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hideDialog();
                Toast.makeText(AdminContactsUpdateDelete.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                switch (service_name){

                    case "doctor":
                        map.put("name", finalname);
                        map.put("chamber", finalarea_chamber_shop);
                        map.put("phone", finalphone);
                        break;

                    case "pharmacy":
                        map.put("name", finalname);
                        map.put("shop", finalarea_chamber_shop);
                        map.put("phone", finalphone);
                        break;

                    case "gas":
                        map.put("name", finalname);
                        map.put("shop", finalarea_chamber_shop);
                        map.put("phone", finalphone);
                        break;

                    case "cng":
                        map.put("name", finalname);
                        map.put("area", finalarea_chamber_shop);
                        map.put("phone", finalphone);

                        break;
                    case "elec":
                        map.put("name", finalname);
                        map.put("area", finalarea_chamber_shop);
                        map.put("phone", finalphone);
                        break;

                    case "tomtom":
                        map.put("name", finalname);
                        map.put("area", finalarea_chamber_shop);
                        map.put("phone", finalphone);
                        break;
                }

                return map;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 8, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    public void contact_delete(String service, String id){
        progressDialog.showDialog();
        String url = getString(R.string.base_url)+"contact/"+service+"/"+id;

        final StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progressDialog.hideDialog();

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    if (message.equals("200")){

                        finish();
                        Toast.makeText(AdminContactsUpdateDelete.this, "ধন্যবাদ, ভুল তথ্য ডিলিট করার জন্য",Toast.LENGTH_LONG).show();

                    }else{

                        Toast.makeText(AdminContactsUpdateDelete.this, message,Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hideDialog();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 8, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance().addToRequestQueue(stringRequest);
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
}
