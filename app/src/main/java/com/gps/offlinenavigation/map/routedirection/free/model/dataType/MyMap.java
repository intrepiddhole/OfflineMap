package com.gps.offlinenavigation.map.routedirection.free.model.dataType;

import com.gps.offlinenavigation.map.routedirection.free.model.util.Constant;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class MyMap
        implements Comparable
{

  private String continent;
  private String country;
  private String mapName;
  private int resId;
  private String size;
  private int status;
  private String url;

  public MyMap(String s)
  {
    int i;
    String s1;
    try
    {
      init();
      status = 1;
      i = s.indexOf("-gh");
      s1 = s;
      if(i > 0)
        s1 = s.substring(0, i);
      mapName = s1;
      generateContinentName(s1);
      File s_1 = new File(Variable.getVariable().getMapsFolder().getAbsolutePath(), (new StringBuilder()).append(s1).append("-gh").toString());
      setUrl(s_1.getAbsolutePath());
      setSize((new StringBuilder()).append(dirSize(s_1)).append("M").toString());
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public MyMap(String s, String s1, String s2)
  {
    try
    {
      init();
      mapName = s;
      size = s1;
      initStatus();
      setUrl((new StringBuilder()).append(s2).append(s).append(".ghz").toString());
      generateContinentName(s);
      return;
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void generateContinentName(String s)
  {
    String as[] = s.split("_");
    setContinent((new StringBuilder()).append(Character.toString(as[0].charAt(0)).toUpperCase()).append(as[0].substring(1)).toString());
    s = "";
    for(int i = 1; i < as.length; i++)
    {
      s = (new StringBuilder()).append(s).append(Character.toString(as[i].charAt(0)).toUpperCase()).append(as[i].substring(1)).append(" ").toString();
    }

    setCountry(s.substring(0, s.length() - 1));
  }

  private void initStatus()
  {
    try
    {
      if(Variable.getVariable().getLocalMapNameList().contains(mapName))
      {
        status = 1;
        return;
      }
    }
    catch(Exception exception)
    {
      exception.printStackTrace();
      return;
    }
    if(Variable.getVariable().getPausedMapName().equalsIgnoreCase(mapName))
    {
      status = 2;
      return;
    }
    status = 3;
    return;
  }

  private void log(String s)
  {
    System.out.println((new StringBuilder()).append(getClass().getSimpleName()).append("-------------------").append(s).toString());
  }

  public int compareTo(MyMap mymap)
  {
    if(getStatus() != 3 && mymap.getStatus() == 3)
    {
      return -1;
    }
    if(getStatus() == 3 && mymap.getStatus() != 3)
    {
      return 1;
    } else
    {
      return (new StringBuilder()).append(getContinent()).append(getCountry()).toString().compareToIgnoreCase((new StringBuilder()).append(mymap.getContinent()).append(mymap.getCountry()).toString());
    }
  }

  public int compareTo(Object obj)
  {
    return compareTo((MyMap)obj);
  }

  public long dirSize(File file)
  {
    if(!file.exists())
    {
      return 0L;
    }
    long l = 0L;
    File[] file_1 = file.listFiles();
    int i = 0;
    while(i < file_1.length)
    {
      if(file_1[i].isDirectory())
      {
        l += dirSize(file_1[i]);
      } else
      {
        l += file_1[i].length();
      }
      i++;
    }
    return l / 0x100000L;
  }

  public String getContinent()
  {
    return continent;
  }

  public String getCountry()
  {
    return country;
  }

  public String getMapName()
  {
    return mapName;
  }

  public String getSize()
  {
    if(size == "")
    {
      return "Map size: < 1M";
    } else
    {
      return (new StringBuilder()).append("Map size: ").append(size).toString();
    }
  }

  public int getStatus()
  {
    return status;
  }

  public String getStatusStr()
  {
    return Constant.statuses[getStatus()];
  }

  public String getUrl()
  {
    return url;
  }

  public void init()
  {
    country = "";
    size = "";
    url = "";
    continent = "";
    mapName = "";
    resId = 0;
  }

  public void setContinent(String s)
  {
    continent = s;
  }

  public void setCountry(String s)
  {
    country = s;
  }

  public void setMapName(String s)
  {
    mapName = s;
  }

  public void setSize(String s)
  {
    size = s;
  }

  public void setStatus(int i)
  {
    status = i;
  }

  public void setUrl(String s)
  {
    url = s;
  }

  public String toString()
  {
    return (new StringBuilder()).append("MyMap{country='").append(country).append('\'').append(", size='").append(size).append('\'').append(", url='").append(url).append('\'').append(", continent='").append(continent).append('\'').append(", mapName='").append(mapName).append('\'').append(", resId=").append(resId).append(", status=").append(getStatusStr()).append('}').toString();
  }
}
