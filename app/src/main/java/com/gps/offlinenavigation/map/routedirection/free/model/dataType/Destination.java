package com.gps.offlinenavigation.map.routedirection.free.model.dataType;

import org.mapsforge.core.model.LatLong;

public class Destination {
  private static Destination destination;
  private LatLong endPoint;
  private LatLong startPoint;

  private Destination(LatLong var1, LatLong var2) {
    this.startPoint = var1;
    this.endPoint = var2;
  }

  public static Destination getDestination() {
    if(destination == null) {
      destination = new Destination((LatLong)null, (LatLong)null);
    }

    return destination;
  }

  public LatLong getEndPoint() {
    return this.endPoint;
  }

  public String getEndPointToString() {
    if(this.endPoint != null) {
      double var1 = this.endPoint.latitude;
      double var3 = this.endPoint.longitude;
      return var1 + "," + var3;
    } else {
      return null;
    }
  }

  public LatLong getStartPoint() {
    return this.startPoint;
  }

  public String getStartPointToString() {
    if(this.startPoint != null) {
      double var1 = this.startPoint.latitude;
      double var3 = this.startPoint.longitude;
      return var1 + "," + var3;
    } else {
      return null;
    }
  }

  public void setEndPoint(LatLong var1) {
    this.endPoint = var1;
  }

  public void setStartPoint(LatLong var1) {
    this.startPoint = var1;
  }
}