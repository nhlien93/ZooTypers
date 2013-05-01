package com.example.zootypers;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 *
 * UI / Activity for leaderboard screen.
 * @author cdallas
 *
 */
public class Leaderboard extends Activity {

  @Override
  protected final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leaderboard);
  }

  @Override
  public final boolean onCreateOptionsMenu(final Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.leaderboard, menu);
    return true;
  }

}
