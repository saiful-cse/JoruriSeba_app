package com.coxtunes.joruriseba.Fire_service;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coxtunes.joruriseba.R;


/**
 * Created by saif on 5/3/2017.
 */

public class Fire_service_call extends Fragment{

    ImageView call1;
    ImageView call2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fire_service_call, container, false);


        call2 = rootView.findViewById(R.id.ramu_firs_call);
        call1 = rootView.findViewById(R.id.cox_cal);
        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aleart1 = new AlertDialog.Builder(getActivity());
                aleart1.setTitle("সতর্কতা !");
                aleart1.setMessage("আপনার কি সরাসরি কক্সবাজার ফায়ার সার্ভিসে ফোন করতে চান?");
                aleart1.setIcon(R.drawable.warning);
                aleart1.setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setData(Uri.parse("tel:+88034164242"));
                        startActivity(callIntent);
                    }
                });
                aleart1.setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dlg = aleart1.create();
                dlg.show();

            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aleart1 = new AlertDialog.Builder(getActivity());
                aleart1.setTitle("সতর্কতা !");
                aleart1.setMessage("আপনার কি সরাসরি রামু ফায়ার সার্ভিসে ফোন করতে করতে চান?");
                aleart1.setIcon(R.drawable.warning);
                aleart1.setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent callIntent = new Intent(Intent.ACTION_VIEW);
                        callIntent.setData(Uri.parse("tel:+88034164242"));
                        startActivity(callIntent);
                    }
                });
                aleart1.setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dlg = aleart1.create();
                dlg.show();

            }
        });

        return rootView;
    }

    public void fire_service_call(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: +88034164242"));
        startActivity(intent);
    }
}
