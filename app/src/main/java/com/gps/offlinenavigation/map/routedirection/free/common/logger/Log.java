package com.gps.offlinenavigation.map.routedirection.free.common.logger;

public class Log {
  public static final int ASSERT = 7;
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int INFO = 4;
  public static final int NONE = -1;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;
  private static LogNode mLogNode;

  public Log() {
  }

  public static void d(String var0, String var1) {
    d(var0, var1, (Throwable)null);
  }

  public static void d(String var0, String var1, Throwable var2) {
    println(3, var0, var1, var2);
  }

  public static void e(String var0, String var1) {
    e(var0, var1, (Throwable)null);
  }

  public static void e(String var0, String var1, Throwable var2) {
    println(6, var0, var1, var2);
  }

  public static LogNode getLogNode() {
    return mLogNode;
  }

  public static void i(String var0, String var1) {
    i(var0, var1, (Throwable)null);
  }

  public static void i(String var0, String var1, Throwable var2) {
    println(4, var0, var1, var2);
  }

  public static void println(int var0, String var1, String var2) {
    println(var0, var1, var2, (Throwable)null);
  }

  public static void println(int var0, String var1, String var2, Throwable var3) {
    if(mLogNode != null) {
      mLogNode.println(var0, var1, var2, var3);
    }

  }

  public static void setLogNode(LogNode var0) {
    mLogNode = var0;
  }

  public static void v(String var0, String var1) {
    v(var0, var1, (Throwable)null);
  }

  public static void v(String var0, String var1, Throwable var2) {
    println(2, var0, var1, var2);
  }

  public static void w(String var0, String var1) {
    w(var0, var1, (Throwable)null);
  }

  public static void w(String var0, String var1, Throwable var2) {
    println(5, var0, var1, var2);
  }

  public static void w(String var0, Throwable var1) {
    w(var0, (String)null, var1);
  }

  public static void wtf(String var0, String var1) {
    wtf(var0, var1, (Throwable)null);
  }

  public static void wtf(String var0, String var1, Throwable var2) {
    println(7, var0, var1, var2);
  }

  public static void wtf(String var0, Throwable var1) {
    wtf(var0, (String)null, var1);
  }
}