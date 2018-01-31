package com.gps.offlinenavigation.map.routedirection.free.model.listeners;

import com.jjoe64.graphview.series.DataPoint;

public abstract interface TrackingListener
{
  public abstract void addDistanceGraphSeriesPoint(DataPoint paramDataPoint1, DataPoint paramDataPoint2);
  
  public abstract void updateAvgSpeed(Double paramDouble);
  
  public abstract void updateDistance(Double paramDouble);
  
  public abstract void updateDistanceGraphSeries(DataPoint[][] paramArrayOfDataPoint);
  
  public abstract void updateMaxSpeed(Double paramDouble);
}