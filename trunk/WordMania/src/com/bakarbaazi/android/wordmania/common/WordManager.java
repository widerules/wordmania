package com.bakarbaazi.android.wordmania.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bakarbaazi.android.wordmania.R;

import android.content.Resources;
import android.util.Log;

public class WordManager {
  private BufferedReader reader = null;
  
  public WordManager(Resources resources) {
    initialize(resources);
  }
  
  public void initialize(Resources resources) {
    reader = new BufferedReader(
        new InputStreamReader(resources.openRawResource(R.raw.all_words)));
  }
  
  public void terminate() {
    try {
      reader.close();
    } catch (IOException e) {
      Log.e("WORD FILE", "can't close the word file", e);
    }
  }
  
  public String getNextWord() {
    try {
      return reader.readLine();
    } catch (IOException e) {
      Log.w("WORDS:", "Not able to read from file.");
    }
    return null;
  }
  
  public String getNextWord(int size) {
    String nextWord = null;
    do {
      nextWord = getNextWord();
    } while (nextWord != null && nextWord.length() < size);
    return nextWord;
  }
}
