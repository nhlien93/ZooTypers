package com.example.zootypers.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import com.example.zootypers.R;
import com.example.zootypers.core.SinglePlayerModel;
import com.example.zootypers.ui.PreGameSelection;
import com.example.zootypers.ui.SinglePlayer;
import com.jayway.android.robotium.solo.Solo;

/**
 * Testing the single player game mode using robotium testing framework.
 * Checking to see if typing a invalid letter and valid letter works. We are
 * also checking to see if typing a complete word works and if the scores are 
 * updated properly; the pause screen and other aspect of single player will 
 * be tested as well.
 * 
 * (White box test since we looked at the Single Player code as well as the UI.)
 * 
 * @author oaknguyen & dyxliang
 *
 */
public class SinglePlayerModelTest extends  ActivityInstrumentationTestCase2<PreGameSelection> {

    private Solo solo;
    private char[] lowChanceLetters = {'j', 'z', 'x', 'q', 'k', 'o'};
    private static final int TIMEOUT = 30000;
    private Button continueButton;

    public SinglePlayerModelTest() {
        super(PreGameSelection.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        continueButton = (Button) getActivity().findViewById(com.example.zootypers.R.id.continue_button);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                continueButton.performClick();
            }
        });
        solo.sleep(1000);
    }

    /**
     * Uses the current words to figure out what to automatically type.
     */
    private void automateKeyboardTyping() {
        List<TextView> textList = getWordsPresented();
        Random randy = new Random();
        int randomValue = randy.nextInt(5);
        TextView currTextView = textList.get(randomValue);
        String currWord = currTextView.getText().toString();
        for (int i = 0; i < currWord.length(); i++) {
            char c = currWord.charAt(i);
            sendKeys(c - 68);
        }
    }

    /**
     * @return a list of the current presented words from the solo class activity.
     */
    private List<TextView> getWordsPresented(){
        solo.sleep(1000);
        List<TextView> retVal = new ArrayList<TextView>();
        retVal.add(((TextView)solo.getCurrentActivity().findViewById(R.id.word0)));
        retVal.add(((TextView)solo.getCurrentActivity().findViewById(R.id.word1)));
        retVal.add(((TextView)solo.getCurrentActivity().findViewById(R.id.word2)));
        retVal.add(((TextView)solo.getCurrentActivity().findViewById(R.id.word3)));
        retVal.add(((TextView)solo.getCurrentActivity().findViewById(R.id.word4)));
        return retVal;
    }

    /**
     * Makes robotium go back to the main screen. Sleeps are to ensure that the activity renders before solo acts.
     */
    private void goBackToMainMenu() {
        final Button pauseButton = (Button) solo.getView(com.example.zootypers.R.id.pause_button);
        solo.sleep(1000);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pauseButton.performClick();
            }
        });
        solo.sleep(1000);
        final Button restartButton = (Button) solo.getView(com.example.zootypers.R.id.restart_button);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restartButton.performClick();
            }
        });
        solo.sleep(1000);
    }

    /**
     * Test the button 'Keyboard' brings the onscreen keyboard on and off window.
     */
    @Test(timeout = TIMEOUT)
    public void testTheKeyboardButtonWorks() {
        final Button keyboardButton = (Button) solo.getView(com.example.zootypers.R.id.keyboard_open_button);
        assertNotNull(keyboardButton);
        solo.sleep(1000);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                keyboardButton.performClick();
            }
        });
        solo.sleep(1000);
        goBackToMainMenu();
    }

    /**
     * Tests the pause button to see if it pauses time as well can continue game.
     */
    @Test(timeout = TIMEOUT)
    public void testPauseButtonWorksProperly() {
        final Button pauseButton = (Button) solo.getView(com.example.zootypers.R.id.pause_button);
        solo.sleep(1000);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pauseButton.performClick();
            }
        });
        solo.sleep(1000);
        TextView timer = (TextView) solo.getView(R.id.time_text);
        CharSequence time = timer.getText();
        solo.sleep(3000);
        assertTrue(time == ((TextView) solo.getView(R.id.time_text)).getText());
        solo.sleep(1000);
        final Button restartButton = (Button) solo.getView(com.example.zootypers.R.id.restart_button);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                restartButton.performClick();
            }
        });
        solo.sleep(1000);
    }

//    /**
//     * Tests the button 'Pause' can go back to main menu
//     */
//    @Test(timeout = TIMEOUT)
//    public void testPauseButtonCanGoToMainMenu() {
//        final Button pauseButton = (Button) solo.getView(com.example.zootypers.R.id.pause_button);
//        solo.sleep(1000);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                pauseButton.performClick();
//            }
//        });
//        solo.sleep(1000);
//        final Button menuButton = (Button) solo.getView(com.example.zootypers.R.id.main_menu_button);
//        solo.sleep(1000);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                menuButton.performClick();
//            }
//        });
//        solo.sleep(1000);
//        solo.searchText("Single Player");
//        final Button singleButton = (Button) solo.getView(com.example.zootypers.R.id.single_player_button);
//        solo.sleep(1000);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                singleButton.performClick();
//            }
//        });
//    }

    /**
     * Tests that the button 'Pause' can initiate a new game.
     */
    @Test(timeout = TIMEOUT)
    public void testPauseButtonCanStartNewGame() {
        final Button pauseButton = (Button) solo.getView(com.example.zootypers.R.id.pause_button);
        solo.sleep(1000);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pauseButton.performClick();
            }
        });
        solo.sleep(1000);
        final Button newGameButton = (Button) solo.getView(com.example.zootypers.R.id.restart_button);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                newGameButton.performClick();
            }
        });
        solo.sleep(1000);
        solo.searchText("Continue");
    }

    /**
     * Tests the amount of word onscreen
     */
    @Test(timeout = TIMEOUT)
    public void testFiveWordsPresent(){
        List<TextView> views = getWordsPresented();
        solo.sleep(3000);
        for(int i = 0; i < 5; i++){
            assertTrue(views.get(i).getText().length() > 0);
        }
        goBackToMainMenu();
    }

    /**
     * Tests the words on the current screen and make sure to type an invalid character
     * that will make the 'Invalid Letter Typed' appear.
     */
    @Test(timeout = TIMEOUT)
    public void testInvalidCharacterPressed(){
        List<TextView> views = getWordsPresented();
        solo.sleep(1000);
        String firstLetters = "";
        for(TextView s : views){
            firstLetters += s.getText().charAt(0);
        }
        solo.sleep(1000);
        for(char c : lowChanceLetters){
            if(firstLetters.indexOf(c) < 0 ){
                sendKeys(c - 68);
                assertTrue(solo.searchText("Invalid Letter Typed"));
            }
        }
        solo.sleep(1000);
        goBackToMainMenu();
    }

    /**
     * Acquire the first letters on the current screen and type the first one to test a valid input.
     */
    @Test(timeout = 60000)
    public void z(){
        List<TextView> views = getWordsPresented();
        TextView s = views.get(0);
        solo.sleep(5000);
        sendKeys(s.getText().charAt(0) - 68);
        views = getWordsPresented();
        solo.sleep(3000);
        CharSequence word = views.get(0).getText();
        solo.sleep(1000);
        SpannableString spanString = new SpannableString(word);
        ForegroundColorSpan[] spans = spanString.getSpans(0, spanString.length(), ForegroundColorSpan.class);
        solo.sleep(3000);
        assertTrue(spans.length > 0);
        goBackToMainMenu();
    }

    /**
     * Gets the current word onscreen and then automatically finish the first one and tests to see if the word at the
     * first position has changed.
     */
    @Test(timeout = TIMEOUT)
    public void testChangeAWordWhenFinished(){
        List<TextView> textList = getWordsPresented();
        TextView currTextView = textList.get(0);
        String currWord = currTextView.getText().toString();
        //Log.v("current-word", currWord);
        for (int i = 0; i < currWord.length(); i++) {
            char c = currWord.charAt(i);
            sendKeys(c - 68);
            //Log.v("current-letter", Character.toString(c));
        }
        textList = getWordsPresented();
        assertTrue(textList.get(0).getText().toString() != currWord);
        goBackToMainMenu();
    }

    /**
     * Test that after automatically finishing words, player gets the correct score.
     */
    @Test(timeout = 60000)
    public void testTypingCorrectWordsThreeTimesUpdateScore() {
        int expectedScore = 0;
        int actualScore = 0;
        for (int i = 0; i < 3; i++) {
            List<TextView> textList = getWordsPresented();
            TextView currTextView = textList.get(0);
            String currWord = currTextView.getText().toString();
            //Log.v("current-word", currWord);
            for (int j = 0; j < currWord.length(); j++) {
                char c = currWord.charAt(j);
                sendKeys(c - 68);               
                //Log.v("current-letter", Character.toString(c));
            }
            TextView score = (TextView) solo.getCurrentActivity().findViewById(R.id.score);
            solo.sleep(1000);
            String scoreString = score.getText().toString();
            solo.sleep(1000);
            expectedScore = 15;
            actualScore = Integer.parseInt(scoreString);
        }
        assertEquals(15, actualScore);
        goBackToMainMenu();
    }

    /**
     * Tests that the post game screen pops up after 1 min.
     */
    @Test(timeout = 90000)
    public void testSimulatePlayingAOneMinuteGame() {
        boolean gameFlag = true;
        while (gameFlag) {
            automateKeyboardTyping();
            if (solo.searchText("New Game") == true) {
                gameFlag = false;
            }
        }
        assertTrue(solo.searchText("New Game"));
        assertTrue(solo.searchText("Main Menu"));
        assertTrue(solo.searchText("Your ad could be here!"));
    }

    /**
     * Tests the initial model when a new game is started. There should be 5 words, 0 score, -1 on both index and word.
     */
    @Test(timeout = TIMEOUT)
    public void testModelInitial() {
        solo.sleep(1000);
        SinglePlayerModel model = ((SinglePlayer) solo.getCurrentActivity()).getModel();
        assertEquals(0, model.getScore());
        assertEquals(5, model.getWordsDisplayed().length);
        assertEquals(-1, model.getCurrWordIndex());
        assertEquals(-1, model.getCurrLetterIndex());
        goBackToMainMenu();
    }

    /**
     * Tests the model after one character is typed. There should still be 5 words, 0 score, and non -1 on both index and word.
     */
    @Test(timeout = TIMEOUT)
    public void testModelAfterOneCharTyped() {
        solo.sleep(1000);
        SinglePlayerModel model = ((SinglePlayer)solo.getCurrentActivity()).getModel();
        List<TextView> views = getWordsPresented();
        TextView s = views.get(0);
        solo.sleep(1000);
        sendKeys(s.getText().charAt(0) - 68);
        assertEquals(0, model.getScore());
        assertEquals(5, model.getWordsDisplayed().length);
        assertEquals(0, model.getCurrWordIndex());
        assertEquals(1, model.getCurrLetterIndex());
        goBackToMainMenu();
    }

    /**
     * Tests the model after one word is finished typing. Should have 5 words displayed, score equal to the word length.
     * The index and word should be back at -1.
     */
    @Test(timeout = TIMEOUT)
    public void testModelAfterOneWordTyped() {
        solo.sleep(1000);
        SinglePlayerModel model = ((SinglePlayer) solo.getCurrentActivity()).getModel();
        List<TextView> views = getWordsPresented();
        TextView s = views.get(0);
        solo.sleep(1000);
        for (int i = 0; i < 5; i++) {
            char c = s.getText().charAt(i);
            sendKeys(c - 68);
            //Log.v("current-letter", Character.toString(c));
        }
        solo.sleep(1000);
        assertEquals(5, model.getScore());
        assertEquals(5, model.getWordsDisplayed().length);
        assertEquals(-1, model.getCurrWordIndex());
        assertEquals(-1, model.getCurrLetterIndex());
        goBackToMainMenu();
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
