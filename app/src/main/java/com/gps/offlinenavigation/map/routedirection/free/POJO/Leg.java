package com.gps.offlinenavigation.map.routedirection.free.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Leg {
  @Expose
  @SerializedName("distance")
  private Distance distance;
  @Expose
  @SerializedName("duration")
  private Duration duration;

  public Leg() {
  }

  public Distance getDistance() {
    return this.distance;
  }

  public Duration getDuration() {
    return this.duration;
  }

  public void setDistance(Distance var1) {
    this.distance = var1;
  }

  public void setDuration(Duration var1) {
    this.duration = var1;
  }
}
