package nl.mprog.apps.EvilHangman6081282;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnMenuItemClickListener{

	public final static String HANGMAN_VARIABLES = "nl.mprog.apps.EvilHangman6081282.HANGMAN_VARIABLES";
	public int misguesses;
	public static int wordsInLibraryWithLength;
	public static String hangmanWord;
	public List<String> hangmanWordList;

	private PopupMenu popupMenu;
	public DatabaseHelper dbhelper = new DatabaseHelper(this);

	private GamePlayInterface gamePlay;
	public GoodGamePlay goodGamePlay;
	public OfflineHighScoresModel offlineHighScoresModel = new OfflineHighScoresModel(dbhelper);

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createDatabase();

		// prevents screen from switching to "landscape" orientation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		startNewGame();
	}

	/* Shows the menu on clicking the menu button */
	@Override
	public void onClick(View v) {
		popupMenu.show();
	}

	/* Starts the corresponding actions when either new game or settings is clicked */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			popupMenu.dismiss();
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

	/* Sets the gamePlay interface */
	public void setGamePlayInterface(GamePlayInterface gpi)
	{
		this.gamePlay = gpi;
	}

	/* Creates database if db is not present */
	public void createDatabase(){
		try {
			dbhelper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* retrieves the sent information from the text field and continues gameplay */
	public void getInput(View view){
		EditText editText = (EditText) findViewById(R.id.edit_message);
		gamePlay.playLetter(editText.getText().toString());
		emptyTextField();
		updateView();
		checkEndGame();
	}

	/* sets the starting display of hangman */
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
	}

	/* Updates the image view to the image that corresponds with the amount of misguesses
	 * left compared to the total amount of misguesses */
	public void updateHangmanImageView(){
		ImageView image = (ImageView) findViewById(R.id.hangman_image);
		Resources res = getResources();
		int totalAmountOfHangmanImages = 6;
		int imageToBeDisplayed = totalAmountOfHangmanImages * gamePlay.getMisguesses() / getMisguesses();
		Drawable drawable = res.getDrawable(R.drawable.hangmanimage6);
		switch(imageToBeDisplayed){
		case 1:
			drawable = res.getDrawable(R.drawable.hangmanimage1);
			break;
		case 2:
			drawable = res.getDrawable(R.drawable.hangmanimage2);
			break;
		case 3:
			drawable = res.getDrawable(R.drawable.hangmanimage3);
			break;
		case 4:
			drawable = res.getDrawable(R.drawable.hangmanimage4);
			break;
		case 5:
			drawable = res.getDrawable(R.drawable.hangmanimage5);
			break;
		}
		if(gamePlay.getMisguesses() == 0){
			drawable = res.getDrawable(R.drawable.hangmanimage0);
		}
		image.setImageDrawable(drawable);
	}

	/* adds artificial whitespaces for displaying the hangman word */ 
	public void updateHangmanWordDisplay(){
		StringBuilder stringBuilder = new StringBuilder("");
		for (int i = 0; i < gamePlay.getWordLength(); i++){
			stringBuilder.append(gamePlay.getHangmanCharacterList().get(i)).append("  ");
		}
		TextView hangmanView = (TextView) findViewById(R.id.hangman_view);
		hangmanView.setText(stringBuilder.toString());
	}

	/* Adds artificial whitespaces to the letters left */
	public void updateLettersLeftDisplay(){
		StringBuilder builder = new StringBuilder();
		int lettersInAlphabet = 26;
		for (int i = 0; i < lettersInAlphabet; i++){
			builder.append(gamePlay.getLettersLeft().get(i)).append("  ");
		}
		TextView letterLeftView = (TextView) findViewById(R.id.letters_left);
		letterLeftView.setText(builder.toString());
	}

	/* updates the display of misguesses left */
	public void updateMisguessesLeftDisplay(){
		TextView hangmanGuessesView = (TextView) findViewById(R.id.hangman_guesses);
		hangmanGuessesView.setText(Integer.toString(gamePlay.getMisguesses()));
	}

	/* updates all views */
	public void updateView(){
		updateHangmanImageView();
		updateHangmanWordDisplay();
		updateMisguessesLeftDisplay();
		updateLettersLeftDisplay();
	}

	/* Returns the Hangmanword that is currently in storage */
	public String getHangmanWord(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		String hangmanWord = sharedPref.getString("hangmanWord", "NoWordPresent");
		return hangmanWord;
	}

	/* gets the amount of misguesses left from internal storage */
	public int getMisguesses(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		int amountOfGuesses = sharedPref.getInt("misguesses", 6);
		return amountOfGuesses;
	}

	/* gets the length of the word from local storage */
	public int getWordLength(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		int length = sharedPref.getInt("wordLength", 7);
		return length;
	}

	/* gets the length of the word from local storage */
	public String getGamePlay(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		String gameplay = sharedPref.getString("gameplay", "Evil");
		return gameplay;
	}

	/* sets the current gameplay to Evil or Good gameplay */
	public void setGamePlay(String gameplay){
		if (gameplay.equals("Good"))
		{
			GamePlayInterface gpi = new GoodGamePlay(getMisguesses(), hangmanWord, wordsInLibraryWithLength, dbhelper);
			setGamePlayInterface(gpi);
		}
		else if(gameplay.equals("Evil"))
		{
			GamePlayInterface gpi = new EvilGamePlay(getMisguesses(), wordsInLibraryWithLength, getWordLength(), hangmanWordList, dbhelper);
			setGamePlayInterface(gpi);
		}
	}

	/* Puts the hangmanWord in internal storage */
	public void setHangmanWord(){
		String word = dbhelper.getDatabaseWord(getWordLength());

		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("hangmanWord", word);
		editor.commit();
	}

	/* Puts the hangmanWord in internal storage */
	public void setScore(int score){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("score", Integer.toString(score));
		editor.commit();
	}

	/* sets the amount of used misguesses locally */
	public void setMisguessesLeft(){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("misguessesUsed", (getMisguesses() - gamePlay.getMisguesses()));
		editor.commit();
	}

	/* puts the word list with all words of the same selected length in class variable */
	public void setHangmanWordList(){
		hangmanWordList = dbhelper.getWordList(getWordLength());
	}

	/* clears the input text field */
	public void emptyTextField(){
		EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.setText("");
	}

	/* restarts the game data */
	public void startNewGame(){
		String gameplay = getGamePlay();
		if(gameplay.equals("Good")){
			misguesses = getMisguesses();
			setHangmanWord();
			hangmanWord = getHangmanWord();
		}
		else if(gameplay.equals("Evil")){
			setHangmanWordList();
		}

		setGamePlay(gameplay);

		setStartingDisplay();
	}

	/* starts the highscoresactivity */
	public void startOnlineHighScoresActivity(){
		setGuessedHangmanWord();
		Intent intent = new Intent(this, OnlineHighScoresActivity.class);
		startActivity(intent);
	}

	/* Starts the offlineHighScoresActivity */
	public void startOfflineHighScoresActivity(){
		setGuessedHangmanWord();
		Intent intent = new Intent(this, OfflineHighScoresActivity.class);
		startActivity(intent);
	}

	/* checks if the game has been won or lost and displays the right corresponding information */
	public void checkEndGame(){
		if(gamePlay.hasLost()){
			DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					startNewGame();
				}
			};
			String title = this.getResources().getString(R.string.game_lost);
			String lostMessage = this.getResources().getString(R.string.lost_message);
			String newGame = this.getResources().getString(R.string.new_game);
			new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(lostMessage)
			.setNegativeButton(newGame , onClick).show();
		}
		else if(gamePlay.hasWon()){
			final int score = gamePlay.getScore();
			setScore(score);
			DialogInterface.OnClickListener onClickOnline = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					startOnlineHighScoresActivity();
				}
			};
			DialogInterface.OnClickListener onClickOffline = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					int usedGuesses = gamePlay.getTotalMisguesses() - gamePlay.getMisguesses();
					offlineHighScoresModel.updateHighScores(score, hangmanWord, usedGuesses, getGamePlay());
					startOfflineHighScoresActivity();
				}
			};
			String title = this.getResources().getString(R.string.game_won);
			String submitOnline = this.getResources().getString(R.string.submit_online);
			String submitOffline = this.getResources().getString(R.string.submit_offline);
			String winMessagePart1 = this.getResources().getString(R.string.win_message_part_1);
			String winMessagePart2 = this.getResources().getString(R.string.win_message_part_2);
			new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(winMessagePart1 + " " + gamePlay.getFinalWord() +"! "+ winMessagePart2 + " " + score +"!")
			.setNegativeButton(submitOnline, onClickOnline)
			.setPositiveButton(submitOffline, onClickOffline)
			.show();
		}
	}

	/* sets the hangmanWord to the guessed evil hangman word if evil gameplay is played */
	public void setGuessedHangmanWord(){
		String gameplay = getGamePlay();
		if(gameplay.equals("Evil")){
			String word = gamePlay.getFinalWord();

			SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("hangmanWord", word);
			editor.commit();
		}
	}

	public void showLoadingGameToast(){
		DisplayToast dt = new DisplayToast(this);
		dt.execute(dbhelper);
	}
}