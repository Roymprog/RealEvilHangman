package nl.mprog.apps.EvilHangman6081282;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HighScoresActivity extends Activity implements OnClickListener {

	public final static String HIGH_SCORES = "nl.mprog.apps.EvilHangman6081282.HIGH_SCORES";
	public final static String HANGMAN_VARIABLES = "nl.mprog.apps.EvilHangman6081282.HANGMAN_VARIABLES";
	
	public HighScore highScore = new HighScore();
	
	public List<List> highScoresData = new ArrayList<List>();
	public List<String> gameplays = new ArrayList<String>();
	public List<String> scores = new ArrayList<String>();
	public List<String> used_guesses = new ArrayList<String>();
	public List<String> words = new ArrayList<String>();
	
	public MainActivity main = new MainActivity();
	
	public List<String> achievedScore = new ArrayList<String>();
	
	// Sets the content layout, fills table with high scores
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setAchievedScore();
		AsynchTask ast = new AsynchTask();
		ast.execute(achievedScore);
		try {
			highScoresData = ast.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fillIndividualDataLists();
		
		// gets all highscores from database and moves to highest score
//		Cursor cur = highScore.getHighScores();
//		cur.moveToFirst();
		
		// column numbers of the corresponding data
		int hangmanWordPosition = 2;
		int misguessesPosition = 3;
		int scorePosition = 4;
		
		// initializes the layout for the table
		TableLayout layout = new TableLayout(this); 
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.setStretchAllColumns(true);
	    
	    // sets textviews
	    TextView rankHeader = new TextView(this);
		TextView wordHeader = new TextView(this);
		TextView misguessesHeader = new TextView(this);
		TextView scoreHeader = new TextView(this);
		TextView gamePlay = new TextView(this);
		
		// sets textiew texts
        rankHeader.setText("Rank");
        rankHeader.setTypeface(Typeface.DEFAULT_BOLD);
        gamePlay.setText("Gameplay");
        gamePlay.setTypeface(Typeface.DEFAULT_BOLD);
        wordHeader.setText("Hangman word");
        wordHeader.setTypeface(Typeface.DEFAULT_BOLD);
        misguessesHeader.setText("Guesses");
        misguessesHeader.setGravity(android.view.Gravity.CENTER);
        misguessesHeader.setTypeface(Typeface.DEFAULT_BOLD);
        scoreHeader.setText("Score");
        scoreHeader.setGravity(android.view.Gravity.CENTER);
        scoreHeader.setTypeface(Typeface.DEFAULT_BOLD);
        
        // adds textviews to row and row to the layout
        TableRow row = new TableRow(this);
        row.addView(rankHeader);
        row.addView(gamePlay);
        row.addView(wordHeader);
        row.addView(misguessesHeader);
        row.addView(scoreHeader);
        layout.addView(row);
        
        // sets number of rows
        int numberOfRows = 10;
        
        // initializes table rows and textview arrays for rank, word, misguesses and score
        TextView[] rank = new TextView[numberOfRows];
        TextView[] word = new TextView[numberOfRows];
        TextView[] misguesses = new TextView[numberOfRows];
        TextView[] score = new TextView[numberOfRows];
        TableRow[] tr = new TableRow[numberOfRows];
        
	    // loops through all rows filling the rows with rank, word, misguesses and score
	    for(int i = 0; i < numberOfRows; i++) {
	        rank[i] = new TextView(this);
	        word[i] = new TextView(this);
	        misguesses[i] = new TextView(this);
	        score[i] = new TextView(this);
	        
	        rank[i].setText("Rank" + Integer.toString(i + 1));
	        word[i].setText(words.get(i));
	        misguesses[i].setText(used_guesses.get(i));
	        misguesses[i].setGravity(android.view.Gravity.CENTER);
	        score[i].setText(scores.get(i));
	        score[i].setGravity(android.view.Gravity.CENTER);
	        
	        tr[i] = new TableRow(this);
	        tr[i].addView(rank[i]);
	        tr[i].addView(word[i]);
	        tr[i].addView(misguesses[i]);
	        tr[i].addView(score[i]);
	        layout.addView(tr[i]);
	        //cur.moveToNext();
	    }
	    
	    // makes a button for new game and adds onclicklistener
	    Button button = new Button(this);
	    button.setText("New game");
	    button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    button.setOnClickListener(this);
	    layout.addView(button);
	    
	    // sets the constructed layout as content
	    setContentView(layout);
	    
		// Show the Up button in the action bar.
		setupActionBar();
	}

	// triggered when new game button is clicked
	@Override
	public void onClick(View v) {
		newGame();
	}
	
	// starts a new game of hangman
	public void newGame(){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void fillIndividualDataLists(){
		gameplays = highScoresData.get(0);
		used_guesses = highScoresData.get(1);
		scores = highScoresData.get(2);
		words = highScoresData.get(3);
	}
	
	public void setAchievedScore(){
		achievedScore.add(getGamePlay());
		achievedScore.add(Integer.toString(getMisguesses()));
		achievedScore.add(getScore());
		achievedScore.add(getHangmanWord());
	}
	
	// gets the length of the word from local storage
	public String getGamePlay(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		String gameplay = sharedPref.getString("gameplay", "Evil");
		return gameplay;
	}
	
	// gets the length of the word from local storage
	public String getScore(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		String score = sharedPref.getString("score", "0");
		return score;
	}
	
	//Returns the Hangmanword that is currently in storage
	public String getHangmanWord(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		String hangmanWord = sharedPref.getString("hangmanWord", "NoWordPresent");
		return hangmanWord;
	}
	
	// gets the amount of misguesses left from internal storage
	public int getMisguesses(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		int amountOfGuesses = sharedPref.getInt("misguesses", 6);
		return amountOfGuesses;
	}
}