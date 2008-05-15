package com.bakarbaazi.android.wordmania.wordjumble;

public class TimeCalculator {
  public static final long MIN_TIME = 10000; //60000;
  
  public long calculate(String word) {
    if (word.length() <= 4) {
      return MIN_TIME;
    } else {
      return MIN_TIME + (word.length() - 4) * 30000;
    }
  }
}
