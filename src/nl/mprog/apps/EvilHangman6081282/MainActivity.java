package nl.mprog.apps.EvilHangman6081282;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnMenuItemClickListener{
		
	public final static String HANGMAN_VARIABLES = "nl.mprog.apps.EvilHangman6081282.HANGMAN_VARIABLES";
	public static int misguesses;
	public static int wordsInLibraryWithLength;
	public static String hangmanWord;
	
	private PopupMenu popupMenu;
	public DatabaseHelper dbhelper = new DatabaseHelper(this);

	private GamePlayInterface gamePlay;
	public GoodGamePlay goodGamePlay;
	public HighScore highScore = new HighScore();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		createDatabase();
		
        // prevents screen from switching to "landscape" orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        startNewGame();
        setStartingDisplay();
	}

	@Override
	public void onClick(View v) {
		popupMenu.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
	       switch (item.getItemId()) {
	       case R.id.new_game:
	    	   		startNewGame();
	    	   		return true;
	       case R.id.settings:
	    	   		Intent intent = new Intent(this, SettingsActivity.class);
	    	   		startActivity(intent);
	    	   		return true;
	       default:
	    	   		return false;
	       }
	}
	
	public void setGamePlayInterface(GamePlayInterface gpi)
	{
		this.gamePlay = gpi;
	}
	// creates the database if database is not present
	public void createDatabase(){
		try {
			dbhelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// retrieves the sent information from the text field and continues gameplay
	public void getInput(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message);
		gamePlay.playLetter(editText.getText().toString());
		emptyTextField();
		updateView();
		checkEndGame();
	}
	
	// sets the starting display of hangman
	public void setStartingDisplay(){
		// shows standard display
		setContentView(R.layout.activity_main);
		
		//shows menu
		popupMenu = new PopupMenu(this, findViewById(R.id.menu_button));
		MenuInflater inflater = popupMenu.getMenuInflater();
		inflater.inflate(R.menu.hangman_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener((OnMenuItemClickListener) this);
        findViewById(R.id.menu_button).setOnClickListener((OnClickListener) this);
		
        updateView();
        
		// shows hangman word (keep this commented, need it later for debugging)
		//TextView textView = (TextView) findViewById(R.id.text_view);
		//textView.setText(gamePlay.hangmanWord);
	}
	
	// adds artificial whitespaces for displaying the hangman word 
	public void updateHangmanWordDisplay(){
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < gamePlay.getWordLength(); i++){
			stringBuilder.append(gamePlay.getHangmanCharacterList().get(i)).append("  ");
		}
		TextView hangmanView = (TextView) findViewById(R.id.hangman_view);
		hangmanView.setText(stringBuilder.toString());
	}
	
	// Adds artificial whitespaces to the letters left
	public void updateLettersLeftDisplay(){
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 26; i++){
			builder.append(gamePlay.getLettersLeft().get(i)).append("  ");
		}
		TextView letterLeftView = (TextView) findViewById(R.id.letters_left);
		letterLeftView.setText(builder.toString());
	}
	
	// updates the display of misguesses left
	public void updateMisguessesLeftDisplay(){
		TextView hangmanGuessesView = (TextView) findViewById(R.id.hangman_guesses);
		hangmanGuessesView.setText(Integer.toString(gamePlay.getMisguesses()));
	}
	
	// updates all views
	public void updateView(){
		updateHangmanWordDisplay();
		updateMisguessesLeftDisplay();
		updateLettersLeftDisplay();
	}
		
	//Returns the Hangmanword that is currently in storage
	public String getHangmanWord(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		String hangmanWord = sharedPref.getString("hangmanWord", "NoWordPresent");
		return hangmanWord;
	}
	
	// gets the amount of misguesses left from internal storage
	public int getMisguesses(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		int amountOfGuesses = sharedPref.getInt("misguesses", 6);
		return amountOfGuesses;
	}
	
	// gets the length of the word from local storage
	public int getWordLength(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		int length = sharedPref.getInt("wordLength", 7);
		return length;
	}
	
	// gets the length of the word from local storage
	public String getGamePlay(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		String gameplay = sharedPref.getString("gameplay", "Evil");
		return gameplay;
	}
	
	// sets the current gameplay to Evil or Good gameplay
	public void setGamePlay(String gameplay){
		if (gameplay.equals("Good"))
        {
        	GamePlayInterface gpi = new GoodGamePlay(misguesses, hangmanWord, wordsInLibraryWithLength);
        	setGamePlayInterface(gpi);
        }
        else if(gameplay.equals("Evil"))
        {
        	GamePlayInterface gpi = new EvilGamePlay(misguesses, hangmanWord, wordsInLibraryWithLength);
        	setGamePlayInterface(gpi);
        }
	}
		
	// Puts the hangmanWord in internal storage
	public void setHangmanWord(){
		String word = dbhelper.getDatabaseWord(getWordLength());
		
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("hangmanWord", word);
		editor.commit();
	}
	
	// clears the input text field
	public void emptyTextField(){
		EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.setText("");
	}
	
	// restarts the game data
	public void startNewGame(){
		misguesses = getMisguesses();
		setHangmanWord();
        hangmanWord = getHangmanWord();
        
        String gameplay = getGamePlay();
        setGamePlay(gameplay);
        
        gamePlay.setSettings();
        setStartingDisplay();
	}
	
	// starts the highscoresactivity
	public void startHighScoresActivity(){
		Intent intent = new Intent(this, HighScoresActivity.class);
    	startActivity(intent);
	}
	
	// checks if the game has been won or lost and displays the right corresponding information
	public void checkEndGame(){
		if(gamePlay.hasLost()){
			new AlertDialog.Builder(this).setTitle("Lost game").setMessage("You have lost a game of hangman. Please press the button to start a new game!").setNegativeButton("New game", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    	startNewGame();
			    }
			}).show();
		}
		else if(gamePlay.hasWon()){
			int score = gamePlay.getScore();
			new AlertDialog.Builder(this).setTitle("Game won!").setMessage("You guessed the word "+ hangmanWord +"! You got a score of "+ score +"!").setNegativeButton("To High Scores!", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    	startHighScoresActivity();
			    }
			}).show();
		}
	}
}