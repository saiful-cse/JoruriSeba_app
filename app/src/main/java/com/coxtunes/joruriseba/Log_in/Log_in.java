package com.coxtunes.joruriseba.Log_in;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.coxtunes.joruriseba.Internet;
import com.coxtunes.joruriseba.MainActivity;
import com.coxtunes.joruriseba.MySingleton;
import com.coxtunes.joruriseba.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Log_in extends AppCompatActivity {

    Button login_button;
    EditText email_input,password_input;

    String email,pass;

    Internet internet;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        internet = new Internet(this);
        login_button = (Button)findViewById(R.id.login);

        email_input = (EditText)findViewById(R.id.input_email);
        password_input = (EditText)findViewById(R.id.input_pass);

        progressBar = findViewById(R.id.progressBar);

        //-------------Login buttton click----------
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_input.getText().toString();
                pass = password_input.getText().toString();

                if (!internet.isConnected()){
                    Toast.makeText(getApplicationContext(), "Please check interet connection.", Toast.LENGTH_SHORT).show();
                }else if(email.equals("") || pass.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter email and password", Toast.LENGTH_SHORT).show();
                }else{
                    admin_login();
                }
            }

        });
        //-------------------------------------------

    }



    public void admin_login() {
        progressBar.setVisibility(View.VISIBLE);
        String url = getString(R.string.server_url)+"Login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("ok")){
                    Toast.makeText(Log_in.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    finish();
                    Intent intent = new Intent(Log_in.this, Loged.class);
                    startActivity(intent);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Log_in.this,response,Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(Log_in.this, (CharSequence) error,Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        MySingleton.getInstance().addToRequestQueue(stringRequest);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.right_out,R.anim.left_in);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.right_out,R.anim.left_in);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
