package com.gps.offlinenavigation.map.routedirection.free;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PrivacyPolicy extends AppCompatActivity {
  public PrivacyPolicy() {
  }

  public void onBackPressed() {
    this.startActivity(new Intent(this, Dashboard.class));
    this.finish();
  }

  protected void onCreate(@Nullable Bundle var1) {
    super.onCreate(var1);
    this.setContentView(2130968604);
    ((Button)this.findViewById(2131624079)).setOnClickListener(new OnClickListener() {
      public void onClick(View var1) {
        Intent var2 = new Intent(PrivacyPolicy.this, Dashboard.class);
        PrivacyPolicy.this.startActivity(var2);
        PrivacyPolicy.this.finish();
      }
    });
  }
}
