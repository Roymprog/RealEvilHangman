package nl.mprog.apps.EvilHangman6081282;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnSeekBarChangeListener{

	public final static String HANGMAN_VARIABLES = "nl.mprog.apps.EvilHangman6081282.HANGMAN_VARIABLES";
	public final static String EXTRA_WORD_LENGTH = "nl.mprog.apps.EvilHangman6081282.EXTRA_WORD_LENGTH"; 
	public final static String EXTRA_MISGUESSES = "nl.mprog.apps.EvilHangman6081282.EXTRA_MISGUESSES";
	public final static String EXTRA_SETTINGS = "nl.mprog.apps.EvilHangman6081282.SETTINGS";

	private SeekBar wordLengthSeekBar;
	private SeekBar misguessesSeekBar;
	private SeekBar gameplaySeekBar;

	public int wordLength;
	public int misguesses;

	/* Sets the content display and initializes the seekbars for the settings */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		initializeSeekBars();
	}

	/* initializes Seekbars with onclicklisteners and sets the progress at right place */
	public void initializeSeekBars(){
		wordLengthSeekBar = (SeekBar) findViewById(R.id.word_length_seek_bar);
		wordLengthSeekBar.setOnSeekBarChangeListener(this);
		wordLengthSeekBar.setProgress(getWordLength());

		misguessesSeekBar = (SeekBar) findViewById(R.id.misguesses_seek_bar);
		misguessesSeekBar.setOnSeekBarChangeListener(this);
		misguessesSeekBar.setProgress(getMisguesses());

		gameplaySeekBar = (SeekBar) findViewById(R.id.gameplay_seek_bar);
		gameplaySeekBar.setOnSeekBarChangeListener(this);
		String gameplay = getGamePlay();

		if(gameplay.equals("Evil")){
			gameplaySeekBar.setProgress(0);
		}
		else if(gameplay.equals("Good")){
			gameplaySeekBar.setProgress(1);
		}
	}

	/* triggered when the seekbar value has changed */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// must be implemented
	}

	/* triggers when seek bar is touched */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		//must be implemented
	}

	/* Upon stopping the change of a seekbar the values get evaluated 
	 * and inserted in the corresponding constant */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBar.setSecondaryProgress(seekBar.getProgress());
		int progress = seekBar.getProgress() + 1;
		Toast toast = Toast.makeText(SettingsActivity.this, "Toast!", Toast.LENGTH_SHORT);
		// handles the word length seekbar
		if (seekBar == wordLengthSeekBar)
		{
			toast.cancel();
			setWordLengthPreference(progress);
			toast = Toast.makeText(SettingsActivity.this, "Word length set to: " + progress, Toast.LENGTH_SHORT);
			toast.show();
		}
		// handles the misguesses seekbar
		else if(seekBar == misguessesSeekBar)
		{
			toast.cancel();
			setMisguessesPreference(progress);
			toast = Toast.makeText(SettingsActivity.this, "Amount of misguesses set to: " + progress, Toast.LENGTH_SHORT);
			toast.show();
		}
		// handles the gameplay seekbar
		else if(seekBar == gameplaySeekBar)
		{
			toast.cancel();
			String gameplay = "";
			if (progress == 1){
				gameplay = "Evil";
			}
			else if (progress == 2){
				gameplay = "Good";
			}
			setGamePlayPreference(gameplay);
			toast =Toast.makeText(SettingsActivity.this, "Gameplay set to: " + gameplay + " gameplay!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/* Moves user back to previous page */
	public void backToGame(View v){
		this.onBackPressed();
	}

	/* Sets the amount of misguesses in internal storage */
	public void setMisguessesPreference(int misguesses)
	{
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("misguesses", misguesses);
		editor.commit();
	}

	/* sets the word lenght in internal storage */
	public void setWordLengthPreference(int wordLength)
	{
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("wordLength", wordLength);
		editor.commit();
	}

	/* sets the gameplay to Evil or Good gameplay */
	public void setGamePlayPreference(String gameplay){
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("gameplay", gameplay);
		editor.commit();
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
}