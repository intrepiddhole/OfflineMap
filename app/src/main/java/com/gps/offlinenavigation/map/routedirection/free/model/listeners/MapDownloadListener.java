package com.gps.offlinenavigation.map.routedirection.free.model.listeners;

public abstract interface MapDownloadListener
{
  public abstract void downloadFinished(String paramString);
  
  public abstract void downloadStart();
  
  public abstract void progressUpdate(Integer paramInteger);
}