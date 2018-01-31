package com.gps.offlinenavigation.map.routedirection.free.common.logger;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class LogView extends TextView implements LogNode {
  LogNode mNext;

  public LogView(Context var1) {
    super(var1);
  }

  public LogView(Context var1, AttributeSet var2) {
    super(var1, var2);
  }

  public LogView(Context var1, AttributeSet var2, int var3) {
    super(var1, var2, var3);
  }

  private StringBuilder appendIfNotNull(StringBuilder var1, String var2, String var3) {
    StringBuilder var4 = var1;
    if(var2 != null) {
      if(var2.length() == 0) {
        var3 = "";
      }

      var4 = var1.append(var2).append(var3);
    }

    return var4;
  }

  public void appendToLog(String var1) {
    this.append("\n" + var1);
  }

  public LogNode getNext() {
    return this.mNext;
  }

  public void println(int var1, String var2, String var3, Throwable var4) {
    String var5 = null;
    switch(var1) {
      case 2:
        var5 = "VERBOSE";
        break;
      case 3:
        var5 = "DEBUG";
        break;
      case 4:
        var5 = "INFO";
        break;
      case 5:
        var5 = "WARN";
        break;
      case 6:
        var5 = "ERROR";
        break;
      case 7:
        var5 = "ASSERT";
    }

    String var6 = null;
    if(var4 != null) {
      var6 = Log.getStackTraceString(var4);
    }

    final StringBuilder var7 = new StringBuilder();
    this.appendIfNotNull(var7, var5, "\t");
    this.appendIfNotNull(var7, var2, "\t");
    this.appendIfNotNull(var7, var3, "\t");
    this.appendIfNotNull(var7, var6, "\t");
    ((Activity)this.getContext()).runOnUiThread(new Thread(new Runnable() {
      public void run() {
        LogView.this.appendToLog(var7.toString());
      }
    }));
    if(this.mNext != null) {
      this.mNext.println(var1, var2, var3, var4);
    }

  }

  public void setNext(LogNode var1) {
    this.mNext = var1;
  }
}
