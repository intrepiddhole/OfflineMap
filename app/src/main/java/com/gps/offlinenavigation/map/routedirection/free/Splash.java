package com.gps.offlinenavigation.map.routedirection.free;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest.Builder;

public class Splash extends AppCompatActivity {
  private InterstitialAd mInterstitialAd;

  public Splash() {
  }

  private boolean isNetworkAvailable() {
    return ((ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
  }

  private void loadInterstitial() {
    AdRequest var1 = (new Builder()).build();
    this.mInterstitialAd.loadAd(var1);
  }

  private InterstitialAd newInterstitialAd() {
    InterstitialAd var1 = new InterstitialAd(this);
    var1.setAdUnitId(this.getString(2131230803));
    var1.setAdListener(new AdListener() {
      public void onAdClosed() {
        Splash.this.goToNextLevel();
      }

      public void onAdFailedToLoad(int var1) {
      }

      public void onAdLoaded() {
      }
    });
    return var1;
  }

  private void showInterstitial() {
    if(this.mInterstitialAd != null && this.mInterstitialAd.isLoaded()) {
      this.mInterstitialAd.show();
    } else {
      this.goToNextLevel();
    }
  }

  public void goToNextLevel() {
    this.startActivity(new Intent(this, Dashboard.class));
    this.finish();
  }

  protected void onCreate(@Nullable Bundle var1) {
    super.onCreate(var1);
    this.requestWindowFeature(1);
    this.getWindow().setFlags(1024, 1024);
    this.setContentView(2130968615);
    this.mInterstitialAd = this.newInterstitialAd();
    this.loadInterstitial();
    (new Splash.MyCountDownTimer(3000L, 1000L)).start();
  }

  private class MyCountDownTimer extends CountDownTimer {
    MyCountDownTimer(long var2, long var4) {
      super(var2, var4);
    }

    public void onFinish() {
      if(Splash.this.isNetworkAvailable()) {
        Splash.this.showInterstitial();
      } else {
        Splash.this.goToNextLevel();
      }
    }

    public void onTick(long var1) {
    }
  }
}