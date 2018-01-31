package com.gps.offlinenavigation.map.routedirection.free.model.util;

public class Calorie {
  public static final double bicycling = 8.0D;
  public static final double running = 7.0D;
  public static final double walking = 3.0D;
  public static final double weightKg = 77.0D;

  public Calorie() {
  }

  public static double CalorieBurned(double var0, double var2) {
    return CalorieBurned(var0, 77.0D, var2);
  }

  public static double CalorieBurned(double var0, double var2, double var4) {
    return var0 * var2 * var4;
  }

  public static double CalorieBurned(double var0, double var2, double var4, double var6, double var8, boolean var10) {
    return var10?(88.362D + 13.397D * var2 + 4.799D * var6 - 5.677D * var8) * var0 * var4:(447.593D + 9.247D * var2 + 3.098D * var6 - 4.33D * var8) * var0 * var4;
  }
}
