package com.gps.offlinenavigation.map.routedirection.free.model.util;

import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gps.offlinenavigation.map.routedirection.free.model.map.Navigator;
import com.graphhopper.util.Instruction;
import com.graphhopper.util.InstructionList;

public class InstructionAdapter extends Adapter<InstructionAdapter.ViewHolder> {
  private InstructionList instructions;

  public InstructionAdapter(InstructionList var1) {
    this.instructions = var1;
  }

  public int getItemCount() {
    return this.instructions.size();
  }

  public void onBindViewHolder(InstructionAdapter.ViewHolder var1, int var2) {
    var1.setItemData(this.instructions.get(var2));
  }

  public InstructionAdapter.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
    return new InstructionAdapter.ViewHolder(LayoutInflater.from(var1.getContext()).inflate(2130968636, var1, false));
  }

  public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
    public TextView description;
    public TextView distance;
    public ImageView icon;
    public TextView time;

    public ViewHolder(View var1) {
      super(var1);
      this.icon = (ImageView)var1.findViewById(2131624204);
      this.description = (TextView)var1.findViewById(2131624205);
      this.distance = (TextView)var1.findViewById(2131624206);
    }

    public void setItemData(Instruction var1) {
      this.icon.setImageResource(Navigator.getNavigator().getDirectionSign(var1));
      this.description.setText(Navigator.getNavigator().getDirectionDescription(var1));
      this.distance.setText(Navigator.getNavigator().getDistance(var1));
    }
  }
}