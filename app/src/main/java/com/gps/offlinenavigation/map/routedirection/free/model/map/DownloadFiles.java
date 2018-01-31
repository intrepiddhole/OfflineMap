package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.os.AsyncTask;
import android.util.Log;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.MyMap;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapDownloadListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.io.File;
import java.net.URL;
import java.util.*;

public class DownloadFiles
{

  private static DownloadFiles downloadFiles;
  private AsyncTask asyncTask;
  private boolean asytaskFinished;
  private List mapDownloadListeners;
  private MapDownloader mapDownloader;

  private DownloadFiles()
  {
    mapDownloadListeners = new ArrayList();
    asytaskFinished = true;
  }

  private void broadcastFinished(String s)
  {
    try
    {
      Variable.getVariable().setDownloadStatus(1);
      for(Iterator iterator = mapDownloadListeners.iterator(); iterator.hasNext(); ((MapDownloadListener)iterator.next()).downloadFinished(s)) { }
      Variable.getVariable().addRecentDownloadedMap(new MyMap(s));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void broadcastOnUpdate(Integer integer)
  {
    for(Iterator iterator = mapDownloadListeners.iterator(); iterator.hasNext(); ((MapDownloadListener)iterator.next()).progressUpdate(integer)) { }
  }

  private void broadcastStart()
  {
    for(Iterator iterator = mapDownloadListeners.iterator(); iterator.hasNext(); ((MapDownloadListener)iterator.next()).downloadStart()) { }
  }

  public static DownloadFiles getDownloader()
  {
    if(downloadFiles == null)
    {
      downloadFiles = new DownloadFiles();
    }
    return downloadFiles;
  }

  private void log(String s)
  {
    Log.i(getClass().getSimpleName(), s);
  }

  public void addListener(MapDownloadListener mapdownloadlistener)
  {
    if(!mapDownloadListeners.contains(mapdownloadlistener))
    {
      mapDownloadListeners.add(mapdownloadlistener);
    }
  }

  public void cancelAsyncTask()
  {
    asyncTask.cancel(true);
  }

  public boolean isAsytaskFinished()
  {
    return asytaskFinished;
  }

  public void removeListener(MapDownloadListener mapdownloadlistener)
  {
    mapDownloadListeners.remove(mapdownloadlistener);
  }

  public void startDownload(final File mapsFolder, final String mapName, final String urlStr)
  {
    mapDownloader = new MapDownloader();
    System.currentTimeMillis();
    asytaskFinished = false;
    asyncTask = (new AsyncTask() {
      protected MapDownloader doInBackground(URL aurl[])
      {
        if(!mapsFolder.exists())
        {
          mapsFolder.mkdirs();
        }
        mapDownloader.downloadFile(urlStr, (new File(mapsFolder.getAbsolutePath(), urlStr.substring(urlStr.lastIndexOf("/") + 1))).getAbsolutePath(), mapName, new MapDownloadListener() {
          public void downloadFinished(String s)
          {
            broadcastFinished(s);
          }

          public void downloadStart()
          {
          }

          public void progressUpdate(Integer integer)
          {
            publishProgress(new Integer[] {
                    integer
            });
          }
        });
        return mapDownloader;
      }

      protected Object doInBackground(Object aobj[])
      {
        return doInBackground((URL[])aobj);
      }

      protected void onCancelled()
      {
        super.onCancelled();
        asytaskFinished = true;
      }

      protected void onPostExecute(MapDownloader mapdownloader)
      {
        super.onPostExecute(mapdownloader);
        System.currentTimeMillis();
      }

      protected void onPostExecute(Object obj)
      {
        onPostExecute((MapDownloader)obj);
      }

      protected void onPreExecute()
      {
        super.onPreExecute();
        asytaskFinished = true;
        broadcastStart();
        Variable.getVariable().setDownloadStatus(0);
      }

      protected void onProgressUpdate(Integer ainteger[])
      {
        super.onProgressUpdate(ainteger);
        broadcastOnUpdate(ainteger[0]);
      }

      protected void onProgressUpdate(Object aobj[])
      {
        onProgressUpdate((Integer[])aobj);
      }
    }).execute(new URL[0]);
  }




/*
    static boolean access$302(DownloadFiles downloadfiles, boolean flag)
    {
        downloadfiles.asytaskFinished = flag;
        return flag;
    }

*/


}
