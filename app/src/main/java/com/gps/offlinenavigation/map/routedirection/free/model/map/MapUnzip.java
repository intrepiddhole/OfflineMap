package com.gps.offlinenavigation.map.routedirection.free.model.map;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MapUnzip {
  public MapUnzip() {
  }

  private void extractFile(ZipInputStream var1, String var2) throws IOException {
    BufferedOutputStream var5 = new BufferedOutputStream(new FileOutputStream(var2));
    byte[] var4 = new byte[8192];

    while(true) {
      int var3 = var1.read(var4);
      if(var3 == -1) {
        var5.close();
        return;
      }

      var5.write(var4, 0, var3);
    }
  }

  public void recursiveDelete(File var1) {
    if(var1.isDirectory()) {
      File[] var2 = var1.listFiles();
      int var4 = var2.length;

      for(int var3 = 0; var3 < var4; ++var3) {
        this.recursiveDelete(var2[var3]);
      }
    }

    try {
      var1.delete();
    } catch (Exception var5) {
      var5.getStackTrace();
    }
  }

  public void unzip(String var1, String var2) throws IOException {
    File var3 = new File(var2);
    if(var3.exists()) {
      this.recursiveDelete(var3);
    }

    if(!var3.exists()) {
      var3.mkdir();
    }

    ZipInputStream var4 = new ZipInputStream(new FileInputStream(var1));

    for(ZipEntry var6 = var4.getNextEntry(); var6 != null; var6 = var4.getNextEntry()) {
      String var5 = var2 + File.separator + var6.getName();
      if(!var6.isDirectory()) {
        this.extractFile(var4, var5);
      } else {
        (new File(var5)).mkdir();
      }

      var4.closeEntry();
    }

    var4.close();
    this.recursiveDelete(new File(var1));
  }
}