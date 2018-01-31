package com.gps.offlinenavigation.map.routedirection.free.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Route {
  @Expose
  @SerializedName("legs")
  private List<Leg> legs = new ArrayList();
  @Expose
  @SerializedName("overview_polyline")
  private OverviewPolyline overviewPolyline;

  public Route() {
  }

  public List<Leg> getLegs() {
    return this.legs;
  }

  public OverviewPolyline getOverviewPolyline() {
    return this.overviewPolyline;
  }

  public void setLegs(List<Leg> var1) {
    this.legs = var1;
  }

  public void setOverviewPolyline(OverviewPolyline var1) {
    this.overviewPolyline = var1;
  }
}
