package com.bakarbaazi.android.wordmania.wordjumble;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Adapter for the list view displaying the words that were found.
 * 
 * @author saurabhsingh
 */
public class FoundWordsAdapter implements ListAdapter {
  private List<String> foundWords = new ArrayList<String>();
  private List<DataSetObserver> observers = new ArrayList<DataSetObserver>();
  private Context context;
  
  public FoundWordsAdapter(Context context) {
    this.context = context;
  }
  
  public void addWord(String word) {
    foundWords.add(word);
    notifyObserversOfChange();
  }
  
  public void clear() {
    foundWords.clear();
    notifyObserversOfChange();
  }
  
  private void notifyObserversOfChange() {
    for (DataSetObserver observer : observers) {
      observer.onChanged();
    }
  }

  @Override
  public boolean areAllItemsSelectable() {
    return false;
  }

  @Override
  public boolean isSelectable(int index) {
    return false;
  }

  @Override
  public int getCount() {
    return foundWords.size();
  }

  @Override
  public Object getItem(int position) {
    return foundWords.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public int getNewSelectionForKey(int currentSelection, int keyCode,
      KeyEvent event) {
    return NO_SELECTION;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    TextView view = new TextView(context);
    view.setText(foundWords.get(position));
    return view;
  }

  @Override
  public void registerDataSetObserver(DataSetObserver observer) {
    observers.add(observer);
  }

  @Override
  public boolean stableIds() {
    return false;
  }

  @Override
  public void unregisterDataSetObserver(DataSetObserver observer) {
    observers.remove(observer);
  }

}
