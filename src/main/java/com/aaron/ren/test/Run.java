package com.aaron.ren.test;

public class Run {

  public static void main(String args[]) {
    Thread thread = new MyThread();
    //thread.setDaemon(true);
    thread.start();

    System.out.println("====main thread exit========");

  }

  private static class MyThread extends Thread {
    @Override
    public void run() {
      while (true) {
        System.out.println("=======thread excude============");
      }

    }
  }
}
