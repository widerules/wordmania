package com.bakarbaazi.android.wordmania.wordjumble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.bakarbaazi.android.wordmania.R;

import android.content.Resources;
import android.util.Log;

public class LegitimacyDeterminer {
  private HashMap<String, String> possibleWords;
  
  public LegitimacyDeterminer(Resources resources, String word) {
    possibleWords = new HashMap<String, String>();
    initPossibleWords(resources, word.toLowerCase());
  }
  
  private void initPossibleWords(Resources resources, String word) {
    HashMap<Character, Integer> original = new HashMap<Character, Integer>();
    for (int i = 0; i < word.length(); i++) {
      char c = word.charAt(i);
      if (original.containsKey(c)) {
        original.put(c, original.get(c) + 1);
      } else {
        original.put(c, 1);
      }
    }
    
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(resources.openRawResource(R.raw.all_words)));
    String newWord = null;
    try {
      while ((newWord = reader.readLine()) != null) {
        HashMap<Character, Integer> newWordMap = 
            new HashMap<Character, Integer>();
        if (newWord.length() <= word.length()) {
          boolean isPossibleWord = true;
          for (int i = 0; i < newWord.length(); i++) {
            char c = newWord.charAt(i);
            if (!original.containsKey(c)) {
              isPossibleWord = false;
              break;
            } else {
              if (newWordMap.containsKey(c)) {
                int count = newWordMap.get(c) + 1;
                if (count <= original.get(c)) {
                  newWordMap.put(c, count);
                } else {
                  isPossibleWord = false;
                  break;
                }
              } else {
                newWordMap.put(c, 1);
              }
            }
          }
          if(isPossibleWord) {
            possibleWords.put(newWord, null);
          }
        }
      }
    } catch (IOException e) {
      Log.w("WORDS:", "Not able to read from file.");
    }
  }
  
  public boolean isLegitimate(String word) {
    return possibleWords.containsKey(word);
  }
  
  public HashMap<String, String> getPossibleWords() {
    return (HashMap<String, String>) possibleWords.clone();
  }
}
