package com.gps.offlinenavigation.map.routedirection.free.model.util;

public class Constant {
  public static final int BUFFER_SIZE = 8192;
  public static final int COMPLETE = 1;
  public static final int DOWNLOADING = 0;
  public static final int ERROR = 4;
  public static final int ON_SERVER = 3;
  public static final int PAUSE = 2;
  public static final String[] statuses = new String[]{"Downloading", "Complete", "Pause", "On server", "Error"};

  public Constant() {
  }
}
