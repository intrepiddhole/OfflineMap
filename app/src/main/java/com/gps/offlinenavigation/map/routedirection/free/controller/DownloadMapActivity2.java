package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.MyMap;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.*;
import com.gps.offlinenavigation.map.routedirection.free.model.map.*;
import com.gps.offlinenavigation.map.routedirection.free.model.util.SetStatusBarColor;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.io.*;
import java.net.URL;
import java.util.*;

public class DownloadMapActivity2 extends AppCompatActivity
        implements MapDownloadListener, OnDownloadingListener, MapFABonClickListener
{

  private TextView downloadStatusTV;
  private ProgressBar itemDownloadPB;
  private int itemPosition;
  private ProgressBar listDownloadPB;
  private TextView listDownloadTV;
  private RecyclerView mapsRV;
  private MyDownloadAdapter myDownloadAdapter;
  private View vh;

  public DownloadMapActivity2()
  {
  }

  private void activeDownload(View view, int i)
  {
    int j;
    MyMap mymap;
    j = Variable.getVariable().getDownloadStatus();
    mymap = myDownloadAdapter.getItem(i);
    if(i != myDownloadAdapter.getPosition(Variable.getVariable().getPausedMapName())) {
      if(vh != view && j != 0 && j != 2)
      {
        vh = view;
        if(mymap.getStatus() == 3)
        {
          ((FloatingActionButton)view.findViewById(0x7f0e0149)).setImageResource(0x7f0200b7);
          downloadStatusTV = (TextView)vh.findViewById(0x7f0e014a);
          downloadStatusTV.setText((new StringBuilder()).append("Downloading ...").append(String.format("%1$3s", new Object[] {
                  "0%"
          })).toString());
          myDownloadAdapter.getItem(itemPosition).setStatus(0);
          initProgressBar((ProgressBar)vh.findViewById(0x7f0e014e));
          DownloadFiles.getDownloader().startDownload(Variable.getVariable().getMapsFolder(), mymap.getMapName(), mymap.getUrl());
          return;
        }
      }
    } else {
      FloatingActionButton view_1 = (FloatingActionButton) view.findViewById(0x7f0e0149);
      if (j != 0) {
        if (j == 2 && DownloadFiles.getDownloader().isAsytaskFinished()) {
          Variable.getVariable().setDownloadStatus(0);
          view_1.setImageResource(0x7f0200b7);
          downloadStatusTV.setText((new StringBuilder()).append("Downloading ...").append(String.format("%1$3s", new Object[]{
                  Integer.valueOf(Variable.getVariable().getMapFinishedPercentage())
          })).append("%").toString());
          DownloadFiles.getDownloader().startDownload(Variable.getVariable().getMapsFolder(), mymap.getMapName(), mymap.getUrl());
        }
      } else {
        Variable.getVariable().setDownloadStatus(2);
        view_1.setImageResource(0x7f0200b8);
        DownloadFiles.getDownloader().cancelAsyncTask();
        downloadStatusTV.setText((new StringBuilder()).append("Paused ...").append(String.format("%1$3s", new Object[]{
                Integer.valueOf(Variable.getVariable().getMapFinishedPercentage())
        })).append("%").toString());
      }
    }
  }

  private void activeRecyclerView(List list)
  {
    mapsRV = (RecyclerView)findViewById(0x7f0e0092);
    Object obj = new DefaultItemAnimator();
    ((DefaultItemAnimator) (obj)).setAddDuration(600L);
    ((DefaultItemAnimator) (obj)).setRemoveDuration(600L);
    mapsRV.setItemAnimator(((android.support.v7.widget.RecyclerView.ItemAnimator) (obj)));
    mapsRV.setHasFixedSize(true);
    obj = new LinearLayoutManager(this);
    mapsRV.setLayoutManager(((android.support.v7.widget.RecyclerView.LayoutManager) (obj)));
    myDownloadAdapter = new MyDownloadAdapter(list, this);
    mapsRV.setAdapter(myDownloadAdapter);
  }

  private void downloadList()
  { //copied from DownloadMapActivity.java
    (new AsyncTask() {
      protected Object doInBackground(Object aobj[])
      {
        return doInBackground((URL[])aobj);
      }

      protected List doInBackground(URL aurl[])
      {
        ArrayList arraylist;
        Iterator iterator;
        int j;
        String s1;
        int i;
        arraylist = new ArrayList();
        ArrayList aurl_1 = downloadMapUrlList(Variable.getVariable().getMapUrlList());
        j = 0;
        iterator = aurl_1.iterator();
        while(iterator.hasNext())
        {
          s1 = (String)iterator.next();
          i = j;
          try
          {
            publishProgress(new Integer[] {
                    Integer.valueOf(0), Integer.valueOf(0)
            });
            i = j;
            URL aurl_2 = new URL(s1);
            i = j;
            publishProgress(new Integer[] {
                    Integer.valueOf(80), Integer.valueOf(0)
            });
            i = j;
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(aurl_2.openStream()));
            int k = 0;
            while(true) {
              i = j;
              if (bufferedreader.readLine() == null) {
                break; /* Loop/switch isn't completed */
              }
              k++;
            }
            i = j;
            bufferedreader.close();
            i = j;
            BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(aurl_2.openStream()));
            i = j;
            publishProgress(new Integer[] {
                    Integer.valueOf(100), Integer.valueOf(0)
            });
            while(true) {
              i = j;
              String s3 = bufferedreader1.readLine();
              if (s3 == null) {
                break; /* Loop/switch isn't completed */
              }
              i = j;
              int l = s3.indexOf("href=\"");
              if (l >= 0) {
                l += 6;
                i = j;
                int i1 = s3.indexOf(".ghz", l);
                if (i1 >= 0) {
                  i = j;
                  int j1 = s3.indexOf("right\">", s3.length() - 52);
                  i = j;
                  int k1 = s3.indexOf("M", j1);
                  i = j;
                  String s2 = s3.substring(l, i1);
                  String s = "";
                  String aurl_3 = s;
                  if (j1 >= 0) {
                    aurl_3 = s;
                    if (k1 >= 0) {
                      i = j;
                      aurl_3 = s3.substring(j1 + 7, k1 + 1);
                      i = j;
                    }
                  }
                  arraylist.add(new MyMap(s2, aurl_3, s1));
                }
              }
              j++;
              i = j;
              publishProgress(new Integer[]{
                      Integer.valueOf(100), Integer.valueOf((int) (((float) j / (float) k) * 100F))
              });
            }
            i = j;
            bufferedreader1.close();
          }
          catch(Exception e)
          {
            e.printStackTrace();
            j = i;
          }
        }
        Collections.sort(arraylist);
        return arraylist;
      }

      protected void onPostExecute(Object obj)
      {
        onPostExecute((List)obj);
      }

      protected void onPostExecute(List list)
      {
        super.onPostExecute(list);
        listReady(list);
        listDownloadPB.setVisibility(View.GONE);
        listDownloadTV.setVisibility(View.GONE);
      }

      protected void onProgressUpdate(Integer ainteger[])
      {
        super.onProgressUpdate(ainteger);
        listDownloadPB.setProgress(ainteger[1].intValue());
        listDownloadPB.setSecondaryProgress(ainteger[0].intValue());
      }

      protected void onProgressUpdate(Object aobj[])
      {
        onProgressUpdate((Integer[])aobj);
      }
    }).execute(new URL[0]);
  }

  private ArrayList downloadMapUrlList(String s)
  {
    ArrayList arraylist = new ArrayList();
    try
    {
      BufferedReader s_1 = new BufferedReader(new InputStreamReader((new URL(s)).openStream()));
      while(true) {
        String s1 = s_1.readLine();
        if (s1 == null) {
          s_1.close();
          return arraylist;
        }
        arraylist.add(s1);
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return arraylist;
    }
  }

  private void initProgressBar(ProgressBar progressbar)
  {
    itemDownloadPB = progressbar;
    progressbar.setProgress(Variable.getVariable().getMapFinishedPercentage());
    progressbar.setMax(100);
    progressbar.setIndeterminate(false);
    progressbar.setVisibility(View.VISIBLE);
  }

  private void listReady(List list)
  {
    if(list.isEmpty())
    {
      Toast.makeText(this, "There is a problem with the server, please report this to app developer!", Toast.LENGTH_SHORT).show();
      return;
    } else
    {
      myDownloadAdapter.clearList();
      myDownloadAdapter.addAll(list);
      return;
    }
  }

  private void log(String s)
  {
    System.out.println((new StringBuilder()).append(getClass().getSimpleName()).append("-------------------").append(s).toString());
  }

  private void printMapsList(List list_1)
  {
    Iterator iterator = list_1.iterator();
    String list = "";
    while(iterator.hasNext()) {
      MyMap obj = (MyMap)iterator.next();
      list = (new StringBuilder()).append(list).append(((MyMap) (obj)).getCountry()).append(", ").toString();
    }
  }

  public void downloadFinished(String s)
  {
    try
    {
      vh = null;
      ((MyMap)myDownloadAdapter.getMaps().get(myDownloadAdapter.getPosition(s))).setStatus(1);
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }

  public void downloadStart()
  {
  }

  public void mapFABonClick(View view)
  {
    try
    {
      itemPosition = mapsRV.getChildAdapterPosition(view);
      activeDownload(view, itemPosition);
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }

  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    setContentView(0x7f04001d);
    (new SetStatusBarColor()).setStatusBarColor(findViewById(0x7f0e0084), getResources().getColor(0x7f0d004c), this);
    OnDownloading.getOnDownloading().setListener(this);
    List bundle_1 = Variable.getVariable().getCloudMaps();
    if(Variable.getVariable().getDownloadStatus() == 0 && bundle != null && !bundle.isEmpty())
    {
      try
      {
        activeRecyclerView(bundle_1);
      }
      catch(Exception e)
      {
        e.getStackTrace();
      }
    } else
    {
      vh = null;
      itemPosition = 0;
      listDownloadPB = (ProgressBar)findViewById(0x7f0e0091);
      listDownloadTV = (TextView)findViewById(0x7f0e0090);
      listDownloadTV.bringToFront();
      listDownloadPB.setProgress(0);
      listDownloadPB.setMax(100);
      listDownloadPB.setIndeterminate(false);
      listDownloadPB.setVisibility(View.VISIBLE);
      listDownloadPB.bringToFront();
      downloadList();
      activeRecyclerView(new ArrayList());
    }
    DownloadFiles.getDownloader().addListener(this);
    Variable.getVariable().setCloudMaps(new ArrayList());
  }

  public boolean onOptionsItemSelected(MenuItem menuitem)
  {
    switch(menuitem.getItemId())
    {
      default:
        return super.onOptionsItemSelected(menuitem);

      case 16908332:
        finish();
        break;
    }
    return true;
  }

  public void onStop()
  {
    super.onStop();
    int i = Variable.getVariable().getDownloadStatus();
    if(i == 0 || i == 2)
    {
      try
      {
        Variable.getVariable().setMapFinishedPercentage(itemDownloadPB.getProgress());
        Variable.getVariable().setCloudMaps(myDownloadAdapter.getMaps());
        vh = null;
        finish();
      }
      catch(Exception exception)
      {
        exception.getStackTrace();
      }
    }
    DownloadFiles.getDownloader().removeListener(this);
    Variable.getVariable().saveVariables();
  }

  public void progressUpdate(Integer integer)
  {
    try
    {
      if(itemDownloadPB != null)
      {
        itemDownloadPB.setProgress(integer.intValue());
        downloadStatusTV.setText((new StringBuilder()).append("Downloading ").append(String.format("%1$3s", new Object[] {
                integer
        })).append("%").toString());
      }
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
  }

  public void progressbarReady(TextView textview, ProgressBar progressbar)
  {
    int i = Variable.getVariable().getDownloadStatus();
    if(i != 0 && i != 2)
      return;
    downloadStatusTV = textview;
    initProgressBar(progressbar);
    vh = (View)progressbar.getParent();
    itemPosition = mapsRV.getChildAdapterPosition(vh);
  }
}
