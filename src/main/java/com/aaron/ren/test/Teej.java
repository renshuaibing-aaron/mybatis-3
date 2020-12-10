package com.aaron.ren.test;

public class Teej {
  public static void main(String[] args) throws InterruptedException {
    try {
      System.out.println(1/0);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }



    Thread.sleep(11);

    try {
      Thread.sleep(11);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("00000");
  }
}
