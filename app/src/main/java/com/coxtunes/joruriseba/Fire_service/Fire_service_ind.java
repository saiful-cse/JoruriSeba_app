package com.coxtunes.joruriseba.Fire_service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coxtunes.joruriseba.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by saif on 5/3/2017.
 */

public class Fire_service_ind extends Fragment{
    private AdView mAdView1;
    InterstitialAd mInterstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fire_service_ind, container, false);

        mAdView1 = (AdView)rootView.findViewById(R.id.adView1);

        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);

        return rootView;
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
