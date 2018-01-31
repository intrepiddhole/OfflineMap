package com.gps.offlinenavigation.map.routedirection.free;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.gps.offlinenavigation.map.routedirection.free.common.logger.Log;
import com.gps.offlinenavigation.map.routedirection.free.common.logger.LogWrapper;

public class SampleActivityBase extends FragmentActivity {
  public static final String TAG = "SampleActivityBase";

  public SampleActivityBase() {
  }

  public void initializeLogging() {
    Log.setLogNode(new LogWrapper());
    Log.i("SampleActivityBase", "Ready");
  }

  protected void onCreate(Bundle var1) {
    super.onCreate(var1);
  }

  protected void onStart() {
    super.onStart();
    this.initializeLogging();
  }
}
