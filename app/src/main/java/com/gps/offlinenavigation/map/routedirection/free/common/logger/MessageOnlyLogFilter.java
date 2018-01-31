package com.gps.offlinenavigation.map.routedirection.free.common.logger;

public class MessageOnlyLogFilter implements LogNode {
  LogNode mNext;

  public MessageOnlyLogFilter() {
  }

  public MessageOnlyLogFilter(LogNode var1) {
    this.mNext = var1;
  }

  public LogNode getNext() {
    return this.mNext;
  }

  public void println(int var1, String var2, String var3, Throwable var4) {
    if(this.mNext != null) {
      this.getNext().println(-1, (String)null, var3, (Throwable)null);
    }

  }

  public void setNext(LogNode var1) {
    this.mNext = var1;
  }
}
