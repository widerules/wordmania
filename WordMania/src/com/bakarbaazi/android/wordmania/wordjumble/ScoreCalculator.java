package com.bakarbaazi.android.wordmania.wordjumble;

public class ScoreCalculator {
  public Score calculate(String word) {
    int length = word.length();
    if (length > 2) {
      int point = 5 + (length - 3) * (length - 3) * 5;
      return new Score(point);
    }
    return new Score(0);
  }
}
