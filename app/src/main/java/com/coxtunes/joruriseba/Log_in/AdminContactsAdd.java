package com.coxtunes.joruriseba.Log_in;

import android.content.Intent;
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
import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.ProgressDialog;
import com.coxtunes.joruriseba.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminContactsAdd extends AppCompatActivity {

    TextView demoName, demoService, demoPhne;
    EditText edname, edarea_shop_chamber, edphone;
    String name, area_chamber_shop, phone;
    Button post;

    Internet internet;

    ProgressDialog progressDialog;

    String service_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contacts_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        service_name = getIntent().getStringExtra("service_name");
        progressDialog = new ProgressDialog(this);

        internet = new Internet(this);

        edname = findViewById(R.id.edPersionName);
        edarea_shop_chamber = findViewById(R.id.edAreaChamberShop);
        edphone = findViewById(R.id.edPh);

        demoName = findViewById(R.id.texName);
        demoService = findViewById(R.id.texService);
        demoPhne = findViewById(R.id.texPhone);

        switch (service_name){

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



        post = findViewById(R.id.btn_post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edname.getText().toString().trim();
                area_chamber_shop = edarea_shop_chamber.getText().toString().trim();
                phone = edphone.getText().toString();

                if (!internet.isConnected()){
                    Toast.makeText(getApplicationContext(), "ইন্টারনেট কানেকশন অন করুন", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty() || area_chamber_shop.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "খালিঘর পুরন করুন", Toast.LENGTH_SHORT).show();
                }else {
                    contacts_add(service_name);
                }
            }
        });


    }

    public void contacts_add(String service)
    {
        progressDialog.showDialog();
        String url = getString(R.string.base_url)+"contact/"+service;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();

                progressDialog.hideDialog();

                try{

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");

                    if (message.equals("201")){

                        finish();
                        Toast.makeText(AdminContactsAdd.this, "ধন্যবাদ, তথ্য সংযুক্ত করার জন্য",Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(AdminContactsAdd.this, message,Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hideDialog();
                Toast.makeText(AdminContactsAdd.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                switch (service_name){

                    case "doctor":
                        map.put("name", name);
                        map.put("chamber", area_chamber_shop);
                        map.put("phone", phone);
                        break;

                    case "pharmacy":
                        map.put("name", name);
                        map.put("shop", area_chamber_shop);
                        map.put("phone", phone);
                        break;

                    case "gas":
                        map.put("name", name);
                        map.put("shop", area_chamber_shop);
                        map.put("phone", phone);
                        break;

                    case "cng":
                        map.put("name", name);
                        map.put("area", area_chamber_shop);
                        map.put("phone", phone);

                        break;
                    case "elec":
                        map.put("name", name);
                        map.put("area", area_chamber_shop);
                        map.put("phone", phone);
                        break;

                    case "tomtom":
                        map.put("name", name);
                        map.put("area", area_chamber_shop);
                        map.put("phone", phone);
                        break;
                }

                return map;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
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
