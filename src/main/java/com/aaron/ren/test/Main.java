package com.aaron.ren.test;

public class Main {
  public static void main(String[] args) {
    int[] a = new int[]{2, 5, 7, 1, 3, 6};
    String[] b = new String[a.length];

    for (int i = 0; i < a.length; i++) {
      int rightAns = 0;
      int sumLeft = creatSumLeft(a, i);

      for (int n = i + 1; n < a.length; n++) {
        rightAns = rightAns + a[n];
        if (sumLeft <= rightAns) {
          break;
        }
      }
      if (sumLeft <= rightAns) {
        b[i] = "L";
      } else {
        b[i] = "R";
      }
    }

    for (int i = 0; i < b.length; i++) {
      System.out.println(b[i]);
    }


  }

  private static int creatSumLeft(int[] a, int i) {
    int sumLeft = 0;
    for (int m = i - 1; m >= 0; m--) {
      sumLeft = sumLeft + a[m];
    }
    return sumLeft;
  }
}
