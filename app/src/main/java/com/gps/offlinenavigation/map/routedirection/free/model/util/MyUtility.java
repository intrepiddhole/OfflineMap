package com.gps.offlinenavigation.map.routedirection.free.model.util;

import java.io.PrintStream;
import org.mapsforge.core.model.LatLong;

public class MyUtility
{

  public MyUtility()
  {
  }

  public static LatLong convertCoordingate(String s)
  {
    String as1[] = new String[2];
    String as[];
    double d;
    double d1;
    if(s.contains(","))
    {
      as = s.split(",");
    } else
    {
      int i = s.indexOf("N");
      int j = s.indexOf("n");
      int k = s.indexOf("S");
      int l = s.indexOf("s");
      if(i > 0)
      {
        as1[0] = s.substring(0, i + 1);
        as1[1] = s.substring(i + 1);
        as = as1;
      } else
      if(j > 0)
      {
        as1[0] = s.substring(0, j + 1);
        as1[1] = s.substring(j + 1);
        as = as1;
      } else
      if(k > 0)
      {
        as1[0] = s.substring(0, k + 1);
        as1[1] = s.substring(k + 1);
        as = as1;
      } else
      {
        as = as1;
        if(l > 0)
        {
          as1[0] = s.substring(0, l + 1);
          as1[1] = s.substring(l + 1);
          as = as1;
        }
      }
    }
    d = toDecimal(as[0]);
    d1 = toDecimal(as[1]);
    System.out.println((new StringBuilder()).append("La: ").append(d).append("\nLo: ").append(d1).toString());
    if(String.valueOf(d).length() == 1 || String.valueOf(d1).length() == 1)
    {
      return null;
    } else
    {
      return new LatLong(d, d1);
    }
  }

  public static LatLong getLatLong(String s)
  {
    if(s.contains("N") || s.contains("S") || s.contains("n") || s.contains("s"))
    {
      return convertCoordingate(s);
    }
    String as[] = new String[2];
    try
    {
      String[] s_1;
      if(s.contains(","))
      {
        s_1 = s.split(",");
      } else {
        s_1 = s.split(" ");
      }
      return new LatLong(Double.parseDouble(s_1[0]), Double.parseDouble(s_1[1]));
    }
    catch(Exception e)
    {
      e.getStackTrace();
    }
    return null;
  }

  public static double toDecimal(String s)
  {
    double d2 = 0.0D;
    double d = d2;
    double d1;
    int i;
    int j;
    long l;
    try
    {
      j = s.indexOf("\260");
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return d;
    }
    d1 = d2;
    if(j > 0)
    {
      d = d2;
      d1 = 0.0D + Double.parseDouble(s.substring(0, j));
      d = d1;
    }
    i = s.indexOf("\u2032");
    d2 = d1;
    if(i > 0)
    {
      d = d1;
      d2 = d1 + Double.parseDouble(s.substring(j + 1, i)) / 60D;
      d = d2;
    }
    j = s.indexOf("\u2033");
    d1 = d2;
    if(j > 0)
    {
      d = d2;
      d1 = d2 + Double.parseDouble(s.substring(i + 1, j)) / 3600D;
      d = d1;
    }
    if(!s.contains("S"))
    {
      d = d1;
      if(!s.contains("s"))
      {
        d = d1;
        if(!s.contains("W"))
        {
          d2 = d1;
          d = d1;
          if(!s.contains("w"))
          {
            l = Math.round(d2 * 100000D);
            return (double)l / 100000D;
          }
          d = d1;
        }
      }
    }
    d2 = Math.abs(d1) * -1D;
    d = d2;
    l = Math.round(d2 * 100000D);
    return (double)l / 100000D;
  }
}
