package com.coxtunes.joruriseba.UserRegistration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.coxtunes.joruriseba.Log_in.Log_in;
import com.coxtunes.joruriseba.Log_in.Loged;
import com.coxtunes.joruriseba.MainActivity;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.ProgressDialog;
import com.coxtunes.joruriseba.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegStep2 extends AppCompatActivity {
    public static final String GOOGLE_ACCOUNT = "google_account";

    SharedPreferences pref;
    ProgressDialog progressDialog;
    GoogleSignInAccount googleSignInAccount;

    String email, name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_step2);

        progressDialog = new ProgressDialog(this);
        pref = getApplicationContext().getSharedPreferences("reg", MODE_PRIVATE);

        googleSignInAccount = getIntent().getParcelableExtra(GOOGLE_ACCOUNT);

        email = googleSignInAccount.getEmail();
        name = googleSignInAccount.getDisplayName();

        registration();

    }

    public void registration() {
        progressDialog.showDialog();
        String url = getString(R.string.base_url)+"registration";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();

                progressDialog.hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    if (message.equals("200")){
                        //Locally saved on app
                        pref.edit().putString("email", email).apply();
                        startActivity(new Intent(RegStep2.this, MainActivity.class));
                        //Toast.makeText(RegStep2.this, "OK",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(RegStep2.this, message,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hideDialog();
                Toast.makeText(RegStep2.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();

                map.put("email", email);
                map.put("name", name);
                return map;

            }
        };
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }


}
