package com.bakarbaazi.android.wordmania;

import com.bakarbaazi.android.wordmania.wordjumble.WordJumble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WordMania extends Activity {
  public static final int ACTIVITY_CREATE = 1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.word_mania_layout);
    
    Button wordMania = (Button) findViewById(R.id.word_jumble_button);
    wordMania.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        Intent i = new Intent(WordMania.this, WordJumble.class);
        startSubActivity(i, ACTIVITY_CREATE);
      }
    });
  }
}