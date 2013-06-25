package nl.mprog.apps.EvilHangman6081282;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class OfflineHighScoresActivity extends Activity implements OnClickListener {

	public final static String HIGH_SCORES = "nl.mprog.apps.EvilHangman6081282.HIGH_SCORES";
	
	public DatabaseHelper dbhelper = new DatabaseHelper(this);
	
	public HighScore highScore = new HighScore(dbhelper);
	
	public TableLayout layout; 
	public Cursor cur;
	
	// Sets the content layout, fills table with high scores
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// prevents screen from switching to "portrait" orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		// gets all highscores from database and moves to highest score
		cur = highScore.getHighScores();
		cur.moveToFirst();
		
		// draw the table headers and fill the rows
		makeTableHeaders();
		makeTableRows();
		
		// creates button for new game and button to online high scores
	    makeNewGameButton();
	    makeToOnlineHighScoresButton();
	    
	    // sets the constructed layout as content
	    setContentView(layout);
	    
		// Show the Up button in the action bar.
		setupActionBar();
	}

	public void makeTableHeaders(){
		// initializes the layout for the table
		layout = new TableLayout(this); 
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.setStretchAllColumns(true);
	    
	    // sets textviews
	    TextView rankHeader = new TextView(this);
	    TextView gamePlayHeader = new TextView(this);
		TextView wordHeader = new TextView(this);
		TextView misguessesHeader = new TextView(this);
		TextView scoreHeader = new TextView(this);
		
		// sets textiew texts
        rankHeader.setText("Rank");
        rankHeader.setTypeface(Typeface.DEFAULT_BOLD);
        gamePlayHeader.setText("Gameplay");
        gamePlayHeader.setTypeface(Typeface.DEFAULT_BOLD);
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
        row.addView(gamePlayHeader);
        row.addView(wordHeader);
        row.addView(misguessesHeader);
        row.addView(scoreHeader);
        layout.addView(row);
	}
	
	public void makeTableRows(){
		// column numbers of the corresponding data
		int gamePlayPosition = 1;
		int hangmanWordPosition = 2;
		int misguessesPosition = 3;
		int scorePosition = 4;	
		
		// sets number of rows
        int numberOfRows = 10;
        
        // initializes table rows and textview arrays for rank, word, misguesses and score
        TextView[] rank = new TextView[numberOfRows];
        TextView[] gamePlay = new TextView[numberOfRows];
        TextView[] word = new TextView[numberOfRows];
        TextView[] misguesses = new TextView[numberOfRows];
        TextView[] score = new TextView[numberOfRows];
        TableRow[] tr = new TableRow[numberOfRows];
        
	    // loops through all rows filling the rows with rank, word, misguesses and score
	    for(int i = 0; i < numberOfRows; i++) {
	        rank[i] = new TextView(this);
	        gamePlay[i] = new TextView(this);
	        word[i] = new TextView(this);
	        misguesses[i] = new TextView(this);
	        score[i] = new TextView(this);
	        
	        rank[i].setText("Rank" + Integer.toString(i + 1));
	        word[i].setText(cur.getString(hangmanWordPosition));
	        if(!cur.isNull(gamePlayPosition)){
	        	gamePlay[i].setText(cur.getString(gamePlayPosition));
	        }
	        misguesses[i].setText(Integer.toString(cur.getInt(misguessesPosition)));
	        misguesses[i].setGravity(android.view.Gravity.CENTER);
	        score[i].setText(Integer.toString(cur.getInt(scorePosition)));
	        score[i].setGravity(android.view.Gravity.CENTER);
	        
	        tr[i] = new TableRow(this);
	        tr[i].addView(rank[i]);
	        tr[i].addView(gamePlay[i]);
	        tr[i].addView(word[i]);
	        tr[i].addView(misguesses[i]);
	        tr[i].addView(score[i]);
	        layout.addView(tr[i]);
	        cur.moveToNext();
	    }
	}
	
	// makes a button for new game and adds onclicklistener
	public void makeNewGameButton(){
	    Button button = new Button(this);
	    button.setText("New game");
	    button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    button.setOnClickListener(new View.OnClickListener(){
	        @Override
	        public void onClick(View v) {
	        	newGame();
	        }
	    });
	    layout.addView(button);
	}
	
	// makes a button for new game and adds onclicklistener
	public void makeToOnlineHighScoresButton(){
	    Button button = new Button(this);
	    button.setText("Online Highscores");
	    button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	    button.setOnClickListener(new View.OnClickListener(){
	        @Override
	        public void onClick(View v) {
	        	Intent intent = new Intent(getApplicationContext(), OnlineHighScoresActivity.class);
	        	startActivity(intent);
	        }
	    });
	    layout.addView(button);
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
}