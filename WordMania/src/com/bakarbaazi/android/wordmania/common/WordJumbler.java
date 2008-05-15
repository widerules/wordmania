package com.bakarbaazi.android.wordmania.common;

import java.util.Random;

import android.util.Log;

public class WordJumbler {
  private static final int SEED = 13;
  
  /**
   * Takes a string and returns a jumbled version of it.
   * 
   * @param word
   * @return
   */
  public String jumble(String word) {
    char[] jumbled = new char[word.length()];
    Random random = new Random(SEED);
    for (int i = 0; i < word.length(); i++) {
      int newIndex = random.nextInt(word.length());
      Log.i("Random Index", "new index " + newIndex);
      putAtIndex(jumbled, word.charAt(i), newIndex);
    }
    return new String(jumbled);
  }
  
  /**
   * Places the provided letter at the specified position. It the position is
   * not empty it is placed at the next free position.
   * 
   * @param jumbled
   * @param letter
   * @param index
   */
  private void putAtIndex(char[] jumbled, char letter, int index) {
    Log.i("Letter ", "" + letter);
    for (int i = index; i < index + jumbled.length; i++) {
      int actualIndex = i % jumbled.length;
      if (jumbled[actualIndex] == 0) {
        jumbled[actualIndex] = letter;
        break;
      }
    }
  }
}
