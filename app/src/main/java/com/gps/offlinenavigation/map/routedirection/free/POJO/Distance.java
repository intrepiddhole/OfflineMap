package com.gps.offlinenavigation.map.routedirection.free.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance {
  @Expose
  @SerializedName("text")
  private String text;
  @Expose
  @SerializedName("value")
  private Integer value;

  public Distance() {
  }

  public String getText() {
    return this.text;
  }

  public Integer getValue() {
    return this.value;
  }

  public void setText(String var1) {
    this.text = var1;
  }

  public void setValue(Integer var1) {
    this.value = var1;
  }
}
