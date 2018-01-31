package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.MyMap;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapFABonClickListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyMapAdapter extends Adapter<MyMapAdapter.ViewHolder> {
  private MapFABonClickListener mapFABonClick;
  private List<MyMap> myMaps;

  public MyMapAdapter(List var1, MapFABonClickListener var2) {
    this.myMaps = var1;
    this.mapFABonClick = var2;
  }

  private void log(String var1) {
    System.out.println("-------" + this.getClass().getSimpleName() + ": " + var1);
  }

  public void addAll(List var1) {
    this.myMaps.addAll(var1);
    this.notifyItemRangeInserted(this.myMaps.size() - var1.size(), var1.size());
  }

  public MyMap getItem(int var1) {
    return (MyMap)this.myMaps.get(var1);
  }

  public int getItemCount() {
    return this.myMaps.size();
  }

  public List getMapNameList() {
    ArrayList var1 = new ArrayList();
    Iterator var2 = this.myMaps.iterator();

    while(var2.hasNext()) {
      var1.add(((MyMap)var2.next()).getMapName());
    }

    return var1;
  }

  public void insert(MyMap var1) {
    if(!this.getMapNameList().contains(var1.getMapName())) {
      this.myMaps.add(var1);
      this.notifyItemInserted(this.getItemCount() - 1);
    }

  }

  public void onBindViewHolder(MyMapAdapter.ViewHolder var1, int var2) {
    var1.setItemData((MyMap)this.myMaps.get(var2));
  }

  public MyMapAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
    return new MyMapAdapter.ViewHolder(LayoutInflater.from(var1.getContext()).inflate(2130968644, var1, false), this.mapFABonClick);
  }

  public MyMap remove(int var1) {
    Object var3 = null;
    MyMap var2 = (MyMap)var3;
    if(var1 >= 0) {
      var2 = (MyMap)var3;
      if(var1 < this.getItemCount()) {
        var2 = (MyMap)this.myMaps.remove(var1);
        this.notifyItemRemoved(var1);
      }
    }

    return var2;
  }

  public void removeAll() {
    int var1 = this.myMaps.size();
    this.myMaps.retainAll(this.myMaps);
    this.notifyItemRangeRemoved(0, var1);
  }

  public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
    public TextView continent;
    public FloatingActionButton flag;
    public View itemView;
    public MapFABonClickListener mapFABonClick;
    public TextView name;
    public TextView size;

    public ViewHolder(View var1, MapFABonClickListener var2) {
      super(var1);
      this.itemView = var1;
      this.mapFABonClick = var2;
      this.flag = (FloatingActionButton)var1.findViewById(2131624272);
      this.name = (TextView)var1.findViewById(2131624273);
      this.continent = (TextView)var1.findViewById(2131624275);
      this.size = (TextView)var1.findViewById(2131624274);
    }

    public void log(String var1) {
      System.out.println("++++" + this.getClass().getSimpleName() + "++++" + var1);
    }

    public void setItemData(MyMap var1) {
      this.itemView.setOnClickListener(new OnClickListener() {
        public void onClick(View var1) {
          ViewHolder.this.log("onclick" + ViewHolder.this.itemView.toString());
          ViewHolder.this.mapFABonClick.mapFABonClick(ViewHolder.this.itemView);
        }
      });
      this.name.setText(var1.getCountry());
      this.continent.setText(var1.getContinent());
      this.size.setText(var1.getSize());
    }
  }
}