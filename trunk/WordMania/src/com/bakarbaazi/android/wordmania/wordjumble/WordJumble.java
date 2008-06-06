package com.bakarbaazi.android.wordmania.wordjumble;

import java.util.HashMap;

import com.bakarbaazi.android.wordmania.R;
import com.bakarbaazi.android.wordmania.common.WordJumbler;
import com.bakarbaazi.android.wordmania.common.WordManager;
import com.bakarbaazi.android.wordmania.common.WordView;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WordJumble extends Activity {
  private static final int MIN_NUM_LETTERS = 5;
  private static final int SMALLEST_WORDS_LENGTH = 3;
  
  private TextView solutionText = null;
  private TextView scoreDisplay = null;
  private TextView statusDisplay = null;
  private TextView remainingTimeDisplay = null;
  private TextView foundWordsDisplay = null;
  private TextView roundScoreDisplay = null;
  private TextView roundTargetDisplay = null;
  
  private WordView jumbledText = null;
  
  private Score currentScore = new Score();
  private Score roundScore = null;
  private ScoreCalculator scoreCalculator = new ScoreCalculator();
  private TimeCalculator timeCalculator = new TimeCalculator();
  private WordManager wordManager = null;
  private WordJumbler jumbler = new WordJumbler();
  private Handler handler = new Handler();
  private LegitimacyDeterminer determiner = null;
  private RoundTracker roundTracker = null;
  private TimerHandler timerHandler = null;  
  
  // The actual word for this round
  private String correctWord = null;
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.word_jumble_layout);
    this.setTitle("Word Jumble");
    
    solutionText = (TextView) findViewById(R.id.solution_word);
    jumbledText = (WordView) findViewById(R.id.jumbled_word);
    scoreDisplay = (TextView) findViewById(R.id.score);
    statusDisplay = (TextView) findViewById(R.id.status_box);
    remainingTimeDisplay = (TextView) findViewById(R.id.time_remaining);
    foundWordsDisplay = (TextView) findViewById(R.id.found_words);
    roundScoreDisplay = (TextView) findViewById(R.id.round_score);
    roundTargetDisplay = (TextView) findViewById(R.id.round_target);
    
    wordManager = new WordManager(this.getResources());
    
    jumbledText.setLetterSelectListener(new JumbledTextHandler());
    
    Button clear = (Button) findViewById(R.id.clear_solution_button);
    clear.setOnClickListener(new View.OnClickListener() {
      public void onClick(View view) {
        solutionText.setText("");
        jumbledText.setDisabled(false);
      }
    });
    
    Button submit = (Button) findViewById(R.id.submit_solution_button);
    submit.setOnClickListener(new SubmitHandler());
    startNewRound();
  }
  
  public void onDestroy() {
    super.onDestroy();
    wordManager.terminate();
  }
  
  /**
   * Determines whether the provided solution is correct of not.
   * 
   * @param solution
   * @return true id the solution is correct.
   */
  private boolean isValidSolution(String solution) {
    if (determiner.isLegitimate(solution.toLowerCase())) {
      return true;
    }
    return false;
  }
  
  /**
   * Starts a new round. Gets the next word to be displayed and shows a jumbled
   * version of it. Resets the solution text box.
   */
  private void startNewRound() {
    correctWord = wordManager.getNextWord(MIN_NUM_LETTERS).toUpperCase();
    if (correctWord == null) {
      // Game over, display dialog and finish on return
      DialogInterface.OnClickListener listener = 
          new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
              setResult(RESULT_OK);
              finish();
            }
          };
      this.showAlert("Game over", 0, "All words exhausted", "OK", listener,
          false, null);
    } else {
      roundScore = new Score(0);
      jumbledText.setWord(jumbler.jumble(correctWord));
      solutionText.setText("");
      statusDisplay.setText("New Round!");
      foundWordsDisplay.setText("");
      roundScoreDisplay.setText("" + roundScore.getScore());
      determiner = new LegitimacyDeterminer(getResources(), correctWord);
      roundTracker = new RoundTracker(determiner.getPossibleWords());
      roundTargetDisplay.setText("" + roundTracker.getCutoffScore().getScore());
      long time = timeCalculator.calculate(correctWord);
      timerHandler = new TimerHandler(time);
      handler.postDelayed(timerHandler, 100);
    }
  }
  
  private void finishRound() {
    if (timerHandler != null) {
      timerHandler.terminate();
    }
    currentScore.add(roundScore);
    scoreDisplay.setText("" + currentScore.getScore());
  }
  
  /**
   * Updates the score at the end of a round.
   */
  private void updateScore(Score score) {
    roundScore.add(score);
    roundScoreDisplay.setText("" + roundScore.getScore());
  }
  
  private boolean isRoundOver() {
    return roundTracker.isRoundOver();
  }
  
  private void gameOver() {
    setResult(RESULT_OK);
    finish();
  }
  
  private class RoundTracker {
    private HashMap<String, String> words;
    private Score cutoffScore;
    
    public RoundTracker(HashMap<String, String> words) {
      this.words = words;
      cutoffScore = calculateCutoffScore(words.keySet());
    }
    
    private Score calculateCutoffScore(Iterable<String> words) {
      int cutoffScore = 0;
      for (String word : words) {
        cutoffScore += scoreCalculator.calculate(word).getScore();
      }
      cutoffScore *= 0.7;
      return new Score(cutoffScore);
    }
    
    public boolean tickOffWord(String word) {
      if (words.containsKey(word)) {
        words.remove(word);
        return true;
      } else {
        return false;
      }
    }
    
    public boolean isRoundOver() {
      if (roundScore.getScore() >= cutoffScore.getScore()) {
        return true;
      }
      return false;
    }
    
    public Score getCutoffScore() {
      return cutoffScore;
    }
    
    public String getRemainingWords() {
      StringBuilder builder = new StringBuilder();
      for (String word : words.keySet()) {
        builder.append(word + ",");
      }
      return builder.toString();
    }
  }
  
  /**
   * Handles the submission of a solution event.
   * 
   * @author saurabhsingh
   */
  private class SubmitHandler implements View.OnClickListener {
    public void onClick(View view) {
      String solution = solutionText.getText().toString();
      if (solution.length() < 3) {
        statusDisplay.setText("The words should at least have "
            + SMALLEST_WORDS_LENGTH + "letters.");
      } else if (!isValidSolution(solution)) {
        statusDisplay.setText(solution + " doesn't seem like a valid word");
      } else {
        foundWordsDisplay.append("," + solution);
        roundTracker.tickOffWord(solution);
        updateScore(scoreCalculator.calculate(solution));
        if (isRoundOver()) {
          finishRound();
          startNewRound();
        }
      }
      solutionText.setText("");
      jumbledText.setDisabled(false);
    }
  }
  
  private class TimerHandler implements Runnable {
    private long startTime;
    private long timeout;
    
    public TimerHandler(long timeout) {
      this.timeout = timeout;
      startTime = SystemClock.uptimeMillis();
    }
    
    public void run() {
      long now = SystemClock.uptimeMillis();
      updateDisplayView(now);
      long millisecondsElapsed = now - startTime;
      if (millisecondsElapsed >= timeout) {
        // Game over, display dialog and finish on return
        DialogInterface.OnClickListener listener = 
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int button) {
                gameOver();
              }
            };
        showAlert("Game over: Time out", 0,
            "Remaining words: " + roundTracker.getRemainingWords(),
            "OK", listener, false, null);
      } else {
        handler.postDelayed(this, 1000);
      }
    }
    
    private void terminate() {
      handler.removeCallbacks(this);
    }
    
    private void updateDisplayView(long now) {
      int secsRemaining = (int)(timeout + startTime - now) / 1000;
      int minutes = secsRemaining / 60;
      long seconds = secsRemaining % 60;
      if (seconds < 10) {
        remainingTimeDisplay.setText(minutes + ":0" + seconds);
      } else {
        remainingTimeDisplay.setText(minutes + ":" + seconds);
      }
    }
  }
  
  private class JumbledTextHandler implements WordView.LetterSelectListener {
    public void onSelect(int index, String s) {
      solutionText.append(s);
      jumbledText.setDisabled(index, true);
    }
  }
}
