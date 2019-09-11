package com.coxtunes.joruriseba.Fire_service;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.coxtunes.joruriseba.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by saif on 5/3/2017.
 */

public class Fire_service_call extends Fragment{

    ImageView c1;
    private AdView mAdView1, mAdView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fire_service_call, container, false);


        c1 = (ImageView)rootView.findViewById(R.id.frs_1);
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aleart1 = new AlertDialog.Builder(getActivity());
                aleart1.setTitle("সতর্কতা !");
                aleart1.setMessage("আপনার কি কোন বিপদ হয়েছে?");
                aleart1.setIcon(R.drawable.warning);
                aleart1.setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder aleart1 = new AlertDialog.Builder(getActivity());
                        aleart1.setTitle("সরাসরি কল! ");
                        aleart1.setMessage("আপনি কি নিশ্চিত কোন বিপদে সম্মুখীন হয়েছেন?");
                        aleart1.setIcon(R.drawable.quickly);
                        aleart1.setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                fire_service_call();
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


        //----------admob-----------
        mAdView1 = (AdView)rootView.findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);

        //---------------------

        return rootView;
    }

    public void fire_service_call(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: +88034164242"));
        startActivity(intent);
    }

    @Override
    public void onPause() {
        if (mAdView1 != null) {
            mAdView1.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView1 != null) {
            mAdView1.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView1 != null) {
            mAdView1.destroy();
        }
        super.onDestroy();
    }
}
