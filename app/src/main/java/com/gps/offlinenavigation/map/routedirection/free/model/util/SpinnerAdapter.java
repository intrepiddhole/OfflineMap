package com.gps.offlinenavigation.map.routedirection.free.model.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.SportCategory;
import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SportCategory> {
  LayoutInflater inflater;
  private ArrayList<SportCategory> list;
  private int resource;

  public SpinnerAdapter(Context var1, int var2, ArrayList<SportCategory> var3) {
    super(var1, var2, var3);
    this.list = var3;
    this.resource = var2;
    this.inflater = (LayoutInflater)var1.getSystemService("layout_inflater");
  }

  public View getDropDownView(int var1, View var2, ViewGroup var3) {
    return this.getView(var1, var2, var3);
  }

  public View getView(int var1, View var2, ViewGroup var3) {
    var2 = this.inflater.inflate(this.resource, var3, false);
    ((ImageView)var2.findViewById(2131624146)).setImageResource(((SportCategory)this.list.get(var1)).getImageId().intValue());
    ((TextView)var2.findViewById(2131624147)).setText(((SportCategory)this.list.get(var1)).getText());
    return var2;
  }
}
