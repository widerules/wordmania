package com.bakarbaazi.android.wordmania.wordjumble;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class WordJumbleView extends View {
  private View jumbledWordBox;
  private View solutionWordBox;
  private View highScore;
  private View clearButton;
  private View submitButton;
  private View currentScore;
  private View remaningTime;
  
  public WordJumbleView(
      Context context, AttributeSet attrs, Map inflateParams) {
    super(context, attrs, inflateParams);
  }
}
