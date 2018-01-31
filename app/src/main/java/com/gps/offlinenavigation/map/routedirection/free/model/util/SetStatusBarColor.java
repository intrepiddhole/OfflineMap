package com.gps.offlinenavigation.map.routedirection.free.model.util;

import android.app.Activity;
import android.os.Build.VERSION;
import android.util.TypedValue;
import android.view.View;

public class SetStatusBarColor {
  public SetStatusBarColor() {
  }

  public int getActionBarHeight(Activity var1) {
    int var3 = 0;
    TypedValue var2 = new TypedValue();
    if(var1.getTheme().resolveAttribute(16843499, var2, true)) {
      var3 = TypedValue.complexToDimensionPixelSize(var2.data, var1.getResources().getDisplayMetrics());
    }

    return var3;
  }

  public int getStatusBarHeight(Activity var1) {
    int var2 = 0;
    int var3 = var1.getResources().getIdentifier("status_bar_height", "dimen", "android");
    if(var3 > 0) {
      var2 = var1.getResources().getDimensionPixelSize(var3);
    }

    return var2;
  }

  public void setStatusBarColor(View var1, int var2, Activity var3) {
    if(VERSION.SDK_INT >= 19) {
      var3.getWindow().setFlags(67108864, 67108864);
      int var4 = this.getActionBarHeight(var3);
      int var5 = this.getStatusBarHeight(var3);
      var1.getLayoutParams().height = var4 + var5;
      var1.setBackgroundColor(var2);
    }

  }

  public void setSystemBarColor(View var1, int var2, Activity var3) {
    if(VERSION.SDK_INT >= 19) {
      var3.getWindow().setFlags(67108864, 67108864);
      var1.setBackgroundColor(var2);
    }

  }
}