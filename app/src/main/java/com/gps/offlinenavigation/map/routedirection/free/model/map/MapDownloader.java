package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.util.Log;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapDownloadListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

// Referenced classes of package com.gps.offlinenavigation.map.routedirection.free.model.map:
//            MapUnzip

public class MapDownloader
{

  private File downloadedFile;
  private long fileLength;
  private boolean startNewDownload;
  private int timeout;

  public MapDownloader()
  {
    fileLength = 0L;
    timeout = 9000;
    startNewDownload = true;
  }

  private HttpURLConnection createConnection(String s)
          throws IOException
  {
    HttpURLConnection s_1 = (HttpURLConnection)(new URL(s)).openConnection();
    s_1.setDoInput(true);
    s_1.setDoOutput(true);
    s_1.setReadTimeout(timeout);
    s_1.setConnectTimeout(timeout);
    return s_1;
  }

  private void prepareDownload(String s, String s1)
          throws IOException
  {
    HttpURLConnection s_1 = createConnection(s);
    downloadedFile = new File(s1);
    String s1_1 = s_1.getHeaderField("Last-Modified");
    fileLength = s_1.getContentLength();
    boolean flag;
    if(!downloadedFile.exists() || downloadedFile.length() >= fileLength || !s1_1.equalsIgnoreCase(Variable.getVariable().getMapLastModified()))
    {
      flag = true;
    } else
    {
      flag = false;
    }
    startNewDownload = flag;
    s_1.disconnect();
  }

  public void downloadFile(String s_p, String s1, String s2, MapDownloadListener mapdownloadlistener)
  {
    BufferedInputStream bufferedinputstream;
    Object obj;
    Object obj1;
    Object obj2;
    byte abyte0[];
    Object obj3;
    Object obj4;
    Object obj5;
    Object obj6;
    Object obj7;
    Object obj8;
    Object obj9;
    int i;
    long l;
    try
    {
      Variable.getVariable().setPausedMapName(s2);
      obj9 = null;
      obj8 = null;
      bufferedinputstream = null;
      obj4 = null;
      obj3 = null;
      obj6 = null;
      obj7 = null;
      obj5 = null;
      l = 0L;
      obj = obj8;
      obj2 = bufferedinputstream;
      obj1 = obj7;
      prepareDownload(s_p, s1);
      obj = obj8;
      obj2 = bufferedinputstream;
      obj1 = obj7;
      HttpURLConnection s = createConnection(s_p);
      obj = s;
      obj2 = bufferedinputstream;
      obj1 = obj7;
      Variable.getVariable().setDownloadStatus(0);
      obj = s;
      obj2 = bufferedinputstream;
      obj1 = obj7;
      if(!startNewDownload)
      {
        obj = s;
        obj2 = bufferedinputstream;
        obj1 = obj7;
        s.setRequestProperty("Range", (new StringBuilder()).append("bytes=").append(downloadedFile.length()).append("-").toString());
        obj = s;
        obj2 = bufferedinputstream;
        obj1 = obj7;
      }
      bufferedinputstream = new BufferedInputStream(s.getInputStream(), 8192);
      obj2 = obj5;
      obj1 = obj6;
      if(startNewDownload) {
        obj2 = obj5;
        obj1 = obj6;
        obj = new FileOutputStream(s1);
        Variable.getVariable().setMapLastModified(s.getHeaderField("Last-Modified"));
      } else {
        obj2 = obj5;
        obj1 = obj6;
        l = 0L + downloadedFile.length();
        obj2 = obj5;
        obj1 = obj6;
        obj = new FileOutputStream(s1, true);
      }
      obj2 = obj;
      obj1 = obj;
      abyte0 = new byte[8192];
      while(true) {
        obj2 = obj;
        obj1 = obj;
        if (Variable.getVariable().getDownloadStatus() != 0) {
          break; /* Loop/switch isn't completed */
        }
        obj2 = obj;
        obj1 = obj;
        i = bufferedinputstream.read(abyte0);
        if (i == -1) {
          break; /* Loop/switch isn't completed */
        }
        l += i;
        obj2 = obj;
        obj1 = obj;
        ((FileOutputStream) (obj)).write(abyte0, 0, i);
        obj2 = obj;
        obj1 = obj;
        mapdownloadlistener.progressUpdate(Integer.valueOf((int) ((100L * l) / fileLength)));
      }
      obj2 = obj;
      obj1 = obj;
      if(l < fileLength) {
        obj2 = obj;
        obj1 = obj;
        Variable.getVariable().setMapFinishedPercentage((int)((100L * l) / fileLength));
      } else {
        obj2 = obj;
        obj1 = obj;
        Variable.getVariable().setDownloadStatus(1);
        obj2 = obj;
        obj1 = obj;
        Variable.getVariable().setPausedMapName("");
        obj2 = obj;
        obj1 = obj;
        (new MapUnzip()).unzip(s1, (new File(Variable.getVariable().getMapsFolder(), (new StringBuilder()).append(s2).append("-gh").toString())).getAbsolutePath());
        obj2 = obj;
        obj1 = obj;
        mapdownloadlistener.downloadFinished(s2);
      }
      if(obj != null)
        ((FileOutputStream) (obj)).close();
      if(bufferedinputstream != null)
        bufferedinputstream.close();
      if(s != null)
        s.disconnect();
    }
    catch(Exception e)
    {
      Variable.getVariable().setDownloadStatus(2);
      e.printStackTrace();
    }
  }

  public void log(String s)
  {
    Log.i(getClass().getSimpleName(), (new StringBuilder()).append("----").append(s).toString());
  }
}
