package com.gps.offlinenavigation.map.routedirection.free.controller;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import java.util.List;

public class FavoriteList {
  public FavoriteList() {
  }

  private void activeRecyclerView(List var1, Activity var2) {
    RecyclerView var4 = (RecyclerView)var2.findViewById(2131624094);
    DefaultItemAnimator var3 = new DefaultItemAnimator();
    var3.setAddDuration(1000L);
    var3.setRemoveDuration(1000L);
    var4.setItemAnimator(var3);
    var4.setHasFixedSize(true);
    var4.setLayoutManager(new LinearLayoutManager(var2));
    (new ItemTouchHelper(new SimpleCallback(0, 12) {
      public boolean onMove(RecyclerView var1, ViewHolder var2, ViewHolder var3) {
        return false;
      }

      public void onSwiped(ViewHolder var1, int var2) {
      }
    })).attachToRecyclerView(var4);
  }
}
