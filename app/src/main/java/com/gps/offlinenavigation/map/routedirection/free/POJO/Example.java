package com.gps.offlinenavigation.map.routedirection.free.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Example {
  @Expose
  @SerializedName("routes")
  private List<Route> routes = new ArrayList();

  public Example() {
  }

  public List<Route> getRoutes() {
    return this.routes;
  }

  public void setRoutes(List<Route> var1) {
    this.routes = var1;
  }
}
