package com.gps.offlinenavigation.map.routedirection.free.model.listeners;

import android.widget.ProgressBar;
import android.widget.TextView;

public abstract interface OnDownloadingListener
{
  public abstract void progressbarReady(TextView paramTextView, ProgressBar paramProgressBar);
}