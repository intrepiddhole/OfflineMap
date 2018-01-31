package com.gps.offlinenavigation.map.routedirection.free.model.map;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.gps.offlinenavigation.map.routedirection.free.model.dataType.MyMap;
import com.gps.offlinenavigation.map.routedirection.free.model.listeners.MapFABonClickListener;
import com.gps.offlinenavigation.map.routedirection.free.model.util.Variable;
import java.util.List;

public class MyDownloadAdapter extends Adapter<MyDownloadAdapter.ViewHolder> {
  private MapFABonClickListener mapFABonClick;
  private List<MyMap> myMaps;

  public MyDownloadAdapter(List<MyMap> var1, MapFABonClickListener var2) {
    this.myMaps = var1;
    this.mapFABonClick = var2;
  }

  public void addAll(List<MyMap> var1) {
    this.myMaps.addAll(var1);
    this.notifyItemRangeInserted(this.myMaps.size() - var1.size(), var1.size());
  }

  public void clearList() {
    this.myMaps.clear();
  }

  public MyMap getItem(int var1) {
    return (MyMap)this.myMaps.get(var1);
  }

  public int getItemCount() {
    return this.myMaps.size();
  }

  public List<MyMap> getMaps() {
    return this.myMaps;
  }

  public int getPosition(String var1) {
    for(int var2 = 0; var2 < this.myMaps.size(); ++var2) {
      if(((MyMap)this.myMaps.get(var2)).getMapName().equalsIgnoreCase(var1)) {
        return var2;
      }
    }

    return -1;
  }

  public void insert(MyMap var1) {
    this.myMaps.add(0, var1);
    this.notifyItemInserted(0);
  }

  public void onBindViewHolder(MyDownloadAdapter.ViewHolder var1, int var2) {
    var1.setItemData((MyMap)this.myMaps.get(var2));
  }

  public MyDownloadAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
    return new MyDownloadAdapter.ViewHolder(LayoutInflater.from(var1.getContext()).inflate(2130968643, var1, false), this.mapFABonClick);
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

  public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
    public TextView continent;
    public TextView downloadStatus;
    public FloatingActionButton flag;
    public MapFABonClickListener mapFABonClick;
    public TextView name;
    public ProgressBar progressBar;
    public TextView size;

    public ViewHolder(View var1, MapFABonClickListener var2) {
      super(var1);
      this.mapFABonClick = var2;
      this.flag = (FloatingActionButton)var1.findViewById(2131624265);
      this.flag.setVisibility(View.VISIBLE);
      this.name = (TextView)var1.findViewById(2131624267);
      this.continent = (TextView)var1.findViewById(2131624269);
      this.size = (TextView)var1.findViewById(2131624268);
      this.downloadStatus = (TextView)var1.findViewById(2131624266);
      this.progressBar = (ProgressBar)var1.findViewById(2131624270);
      this.progressBar.setVisibility(View.GONE);
    }

    public void setItemData(MyMap var1) {
      switch(var1.getStatus()) {
        case 0:
          this.flag.setVisibility(View.VISIBLE);
          this.flag.setImageResource(2130837687);
          this.downloadStatus.setText("Downloading ..." + String.format("%1$3s", new Object[]{Integer.valueOf(Variable.getVariable().getMapFinishedPercentage())}) + "%");
          this.progressBar.setVisibility(View.GONE);
          OnDownloading.getOnDownloading().setDownloadingProgressBar(this.downloadStatus, this.progressBar);
          break;
        case 1:
          this.flag.setVisibility(View.GONE);
          this.downloadStatus.setText("Download Complete");
          this.progressBar.setVisibility(View.GONE);
          break;
        case 2:
          this.flag.setVisibility(View.VISIBLE);
          this.flag.setImageResource(2130837688);
          this.downloadStatus.setText("Paused ..." + String.format("%1$3s", new Object[]{Integer.valueOf(Variable.getVariable().getMapFinishedPercentage())}) + "%");
          this.progressBar.setVisibility(View.GONE);
          OnDownloading.getOnDownloading().setDownloadingProgressBar(this.downloadStatus, this.progressBar);
          break;
        default:
          this.flag.setVisibility(View.VISIBLE);
          this.flag.setImageResource(2130837659);
          this.downloadStatus.setText("Download");
          this.progressBar.setVisibility(View.GONE);
      }

      this.name.setText(var1.getCountry());
      this.continent.setText(var1.getContinent());
      this.size.setText(var1.getSize());
      this.flag.setOnClickListener(new OnClickListener() {
        public void onClick(View var1) {
          ViewHolder.this.mapFABonClick.mapFABonClick(ViewHolder.this.itemView);
        }
      });
    }
  }
}