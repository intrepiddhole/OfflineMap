package com.gps.offlinenavigation.map.routedirection.free.model.dataType;

public class SportCategory {
  private Integer imageId;
  private double sportMET;
  private String text;

  public SportCategory(String var1, Integer var2, double var3) {
    this.text = var1;
    this.imageId = var2;
    this.sportMET = var3;
  }

  public Integer getImageId() {
    return this.imageId;
  }

  public double getSportMET() {
    return this.sportMET;
  }

  public String getText() {
    return this.text;
  }

  public void setImageId(Integer var1) {
    this.imageId = var1;
  }

  public void setText(String var1) {
    this.text = var1;
  }
}
