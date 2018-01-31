package com.gps.offlinenavigation.map.routedirection.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest.Builder;

public class LockActivity extends AppCompatActivity {
  AdView adView;
  ImageButton btnnoapp;
  ImageButton btnyesapp;

  public LockActivity() {
  }

  public void onBackPressed() {
  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
    this.requestWindowFeature(1);
    this.getWindow().setFlags(1024, 1024);
    this.setContentView(2130968606);
    this.adView = (AdView)this.findViewById(2131624086);
    AdRequest var2 = (new Builder()).build();
    this.adView.loadAd(var2);
    this.btnnoapp = (ImageButton)this.findViewById(2131624089);
    this.btnyesapp = (ImageButton)this.findViewById(2131624088);
    this.btnnoapp.setOnClickListener(new OnClickListener() {
      public void onClick(View var1) {
        Intent var2 = new Intent(LockActivity.this, Dashboard.class);
        LockActivity.this.startActivity(var2);
        LockActivity.this.finish();
      }
    });
    this.btnyesapp.setOnClickListener(new OnClickListener() {
      public void onClick(View var1) {
        LockActivity.this.finish();
      }
    });
  }
}