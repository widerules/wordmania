package com.bakarbaazi.android.wordmania.wordjumble;

public class Score {
  private int score = 0;
  
  public Score(int score) {
    this.score = score;
  }
  
  public Score() {
  }
  
  public int getScore() {
    return score;
  }
  
  public void add(Score score) {
    this.score += score.score;
  }
}
