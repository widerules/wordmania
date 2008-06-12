package com.bakarbaazi.android.wordmania.common;

import java.util.Map;

import com.bakarbaazi.android.wordmania.R;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

public class WordView extends TableLayout {
  private static final int MAX_LENGTH = 8;
  private static final int CORNER_RADIUS = 5;
  private Button[] letters;
  private LetterSelectListener listener = null;
  
  public WordView(Context context, AttributeSet attrs, Map params) {
    super(context, attrs, params);
    
    TableRow row = new TableRow(context );
    row.setPadding(0, 0, 0, 0);
    letters = new Button[MAX_LENGTH];
        
    for (int i = 0; i < MAX_LENGTH; i++) {
      letters[i] = new Button(context);
      PaintDrawable background = 
        (PaintDrawable) this.getResources().getDrawable(
            R.drawable.enabled);
      background.setCornerRadius(CORNER_RADIUS);
      
      letters[i].setBackground(background);
      letters[i].setPadding(7, 5, 7, 5);
      EventHandler eventHandler = new EventHandler(i);
      letters[i].setOnClickListener(eventHandler);
      letters[i].setOnFocusChangeListener(eventHandler);
      letters[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
      letters[i].setTextScaleX(1.2F);
      
      TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
      layoutParams.setMargins(1, 1, 1, 1);
      
      row.addView(letters[i], layoutParams);
    }
    this.addView(row, new TableLayout.LayoutParams());
  }
  
  public void setWord(String word) {
    if (word.length() > MAX_LENGTH) {
      word = word.substring(0, MAX_LENGTH);
    }
    for (int i = 0; i < letters.length; i++) {
      if (i < word.length()) {
        letters[i].setText("" + word.charAt(i));
      } else {
        letters[i].setText("");
      }
    }
  }
  
  public void setDisabled(int letter, boolean state) {
    PaintDrawable background = null;
    if (state) {
      background = (PaintDrawable) this.getResources().getDrawable(
            R.drawable.disabled);
    } else {
      background = (PaintDrawable) this.getResources().getDrawable(
            R.drawable.enabled);
    }
    background.setCornerRadius(CORNER_RADIUS);
    letters[letter].setBackground(background);
    letters[letter].setClickable(!state);
  }
  
  public void setDisabled(boolean state) {
    for (int i = 0; i < letters.length; i++) {
      setDisabled(i, state);
    }
  }
  
  public void setLetterSelectListener(LetterSelectListener listener) {
    this.listener = listener;
  }
  
  public interface LetterSelectListener {
    public void onSelect(int index, String c);
  }
  
  private class EventHandler
      implements View.OnClickListener, View.OnFocusChangeListener {
    private int index;
    
    public EventHandler(int index) {
      this.index = index;
    }
    
    public void onClick(View view) {
      if (listener != null) {
        listener.onSelect(index, ((Button) view).getText().toString());
      }
    }
    
    public void onFocusChanged(View view, boolean hasFocus) {
      
    }
  }
}
