package com.coxtunes.joruriseba.Log_in;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.coxtunes.joruriseba.MainActivity;
import com.coxtunes.joruriseba.R;
import com.coxtunes.joruriseba.Wifi;

public class Loged extends AppCompatActivity {

    CardView cardTomtom, cardDoc, cardFer, cardCn, cardGa,
            cardElec, cardPoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged);

        //-------card click------------

        cardTomtom = findViewById(R.id.cardTomtom);
        cardTomtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Loged.this, AdminFetchAllContacts.class);
                intent.putExtra("service_name", "tomtom");
                startActivity(intent);
            }
        });

        cardDoc = findViewById(R.id.cardDoctor);
        cardDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loged.this, AdminFetchAllContacts.class);
                intent.putExtra("service_name", "doctor");
                startActivity(intent);
            }
        });
        cardFer = findViewById(R.id.cardFermacy);
        cardFer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loged.this, AdminFetchAllContacts.class);
                intent.putExtra("service_name", "pharmacy");
                startActivity(intent);

            }
        });
        cardCn = findViewById(R.id.cardCng);
        cardCn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loged.this, AdminFetchAllContacts.class);
                intent.putExtra("service_name", "cng");
                startActivity(intent);

            }
        });
        cardGa = findViewById(R.id.cardGas);
        cardGa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loged.this, AdminFetchAllContacts.class);
                intent.putExtra("service_name", "gas");
                startActivity(intent);
            }
        });
        cardElec = findViewById(R.id.cardElectrician);
        cardElec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Loged.this, AdminFetchAllContacts.class);
                intent.putExtra("service_name", "elec");
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("লগ আউট করবেন ?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }
}
