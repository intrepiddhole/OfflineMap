package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.widget.ProgressBar;
import android.widget.TextView;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.OnDownloadingListener;

public class OnDownloading {
  private static OnDownloading onDownloading;
  private ProgressBar downloadingProgressBar = null;
  private OnDownloadingListener listener;

  private OnDownloading() {
  }

  public static OnDownloading getOnDownloading() {
    if(onDownloading == null) {
      onDownloading = new OnDownloading();
    }

    return onDownloading;
  }

  public ProgressBar getDownloadingProgressBar() {
    return this.downloadingProgressBar;
  }

  public void setDownloadingProgressBar(TextView var1, ProgressBar var2) {
    this.downloadingProgressBar = var2;
    this.listener.progressbarReady(var1, var2);
  }

  public void setListener(OnDownloadingListener var1) {
    this.listener = var1;
  }
}
