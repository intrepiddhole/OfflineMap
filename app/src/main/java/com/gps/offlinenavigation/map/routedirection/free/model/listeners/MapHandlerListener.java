package com.gps.offlinenavigation.map.routedirection.free.model.listeners;

import org.mapsforge.core.model.LatLong;

public abstract interface MapHandlerListener
{
  public abstract void onPressLocation(LatLong paramLatLong);
  
  public abstract void pathCalculating(boolean paramBoolean);
}