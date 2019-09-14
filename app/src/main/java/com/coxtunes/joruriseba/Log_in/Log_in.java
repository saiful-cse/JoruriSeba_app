package com.coxtunes.joruriseba.Log_in;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MainActivity;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.ProgressDialog;
import com.coxtunes.joruriseba.R;


import org.json.JSONException;
import org.json.JSONObject;



public class Log_in extends AppCompatActivity {

    Button login_button;
    EditText pin_input;

    String pin;

    Internet internet;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        progressDialog = new ProgressDialog(this);
        internet = new Internet(this);
        login_button = findViewById(R.id.btnLogin);
        pin_input = findViewById(R.id.edPassword);

        //-------------Login buttton click----------
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pin = pin_input.getText().toString();

                if (!internet.isConnected()){
                    Toast.makeText(getApplicationContext(), "Please check internet connection.", Toast.LENGTH_SHORT).show();
                }else if(pin.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter pin", Toast.LENGTH_SHORT).show();
                }else{
                    admin_login();
                }
            }

        });
        //-------------------------------------------

    }



    public void admin_login() {
        progressDialog.showDialog();
        String url = getString(R.string.base_url)+"admin/"+pin;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.hideDialog();
                try{

                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    if (message.equals("200")){
                        startActivity(new Intent(Log_in.this, Loged.class));
                        Toast.makeText(Log_in.this, "OK",Toast.LENGTH_SHORT).show();

                    }else{

                        Toast.makeText(Log_in.this, message,Toast.LENGTH_LONG).show();

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hideDialog();
                Toast.makeText(Log_in.this, (CharSequence) error,Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 8, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }
}
