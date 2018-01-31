package com.gps.offlinenavigation.map.routedirection.free.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverviewPolyline {
  @Expose
  @SerializedName("points")
  private String points;

  public OverviewPolyline() {
  }

  public String getPoints() {
    return this.points;
  }

  public void setPoints(String var1) {
    this.points = var1;
  }
}
