package com.gps.offlinenavigation.map.routedirection.free;

import com.google.android.gms.maps.model.LatLng;
import java.util.*;
import org.json.*;

public class DataParser
{

  public DataParser()
  {
  }

  private List decodePoly(String s)
  {
    ArrayList arraylist;
    int i;
    int j;
    int k;
    int k1;
    int l;
    int i1;
    int j1;
    arraylist = new ArrayList();
    i = 0;
    k1 = s.length();
    k = 0;
    j = 0;
    while(i < k1) {
      l = 0;
      i1 = 0;
      j1 = i;
      while (true) {
        i = j1 + 1;
        j1 = s.charAt(j1) - 63;
        i1 |= (j1 & 0x1f) << l;
        l += 5;
        if (j1 < 32) {
          break;
        }
        j1 = i;
      }
      if ((i1 & 1) != 0) {
        l = ~(i1 >> 1);
      } else {
        l = i1 >> 1;
      }
      j1 = k + l;
      k = 0;
      l = 0;
      i1 = i;
      while (true) {
        i = i1 + 1;
        i1 = s.charAt(i1) - 63;
        l |= (i1 & 0x1f) << k;
        k += 5;
        if (i1 < 32)
          break;
        i1 = i;
      }
      if ((l & 1) != 0) {
        k = ~(l >> 1);
      } else {
        k = l >> 1;
      }
      j += k;
      arraylist.add(new LatLng((double) j1 / 100000D, (double) j / 100000D));
      k = j1;
    }
    return arraylist;
  }

  public List parse(JSONObject jsonobject_1)
  {
    ArrayList arraylist = new ArrayList();
    try {
      JSONArray jsonobject = jsonobject_1.getJSONArray("routes");
      int i = 0;
      while (i < jsonobject.length()) {
        JSONArray jsonarray;
        ArrayList arraylist1;
        jsonarray = ((JSONObject) jsonobject.get(i)).getJSONArray("legs");
        arraylist1 = new ArrayList();
        int j = 0;
        while (j < jsonarray.length()) {
          JSONArray jsonarray1 = ((JSONObject) jsonarray.get(j)).getJSONArray("steps");
          int k = 0;
          while (k < jsonarray1.length()) {
            List list = decodePoly((String) ((JSONObject) ((JSONObject) jsonarray1.get(k)).get("polyline")).get("points"));
            int l = 0;
            while (l < list.size()) {
              HashMap hashmap = new HashMap();
              hashmap.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
              hashmap.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
              arraylist1.add(hashmap);
              l++;
            }
            k++;
          }
          arraylist.add(arraylist1);
          j++;
        }
        i++;
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
    return arraylist;
  }
}
