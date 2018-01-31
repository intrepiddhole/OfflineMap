package com.gps.offlinenavigation.map.routedirection.free.common.logger;

import android.util.Log;

public class LogWrapper implements LogNode {
  private LogNode mNext;

  public LogWrapper() {
  }

  public LogNode getNext() {
    return this.mNext;
  }

  public void println(int var1, String var2, String var3, Throwable var4) {
    String var5 = var3;
    if(var3 == null) {
      var5 = "";
    }

    String var6 = var3;
    if(var4 != null) {
      var6 = var3 + "\n" + Log.getStackTraceString(var4);
    }

    Log.println(var1, var2, var5);
    if(this.mNext != null) {
      this.mNext.println(var1, var2, var6, var4);
    }

  }

  public void setNext(LogNode var1) {
    this.mNext = var1;
  }
}
