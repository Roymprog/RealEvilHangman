package nl.mprog.apps.EvilHangman6081282;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
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
	
	public int wordLength;
	public int misguesses;
	
	// Sets the content display and initializes the seekbars for the settings
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		wordLengthSeekBar = (SeekBar) findViewById(R.id.word_length_seek_bar);
		wordLengthSeekBar.setOnSeekBarChangeListener(this);
		
		misguessesSeekBar = (SeekBar) findViewById(R.id.misguesses_seek_bar);
		misguessesSeekBar.setOnSeekBarChangeListener(this);
		
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	// triggered when the seekbar value has changed
	@Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Toast.makeText(DisplayMessageActivity.this, "Seekbar Value : " + progress, Toast.LENGTH_SHORT).show();
    }

	// triggers when seek bar is touched
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //Toast.makeText(DisplayMessageActivity.this, "Started Tracking Seekbar", Toast.LENGTH_SHORT).show();
    }
    
    // Upon stopping the change of a seekbar the values get evaluated and inserted in the corresponding constant
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    	seekBar.setSecondaryProgress(seekBar.getProgress());
    	int progress = seekBar.getProgress() + 1;
    	if (seekBar == wordLengthSeekBar)
    	{
    		//wordLength = progress;
    		setWordLengthPreference(progress);
	        Toast.makeText(SettingsActivity.this, "Word length set to: " + progress, Toast.LENGTH_SHORT).show();
    	}
    	else if(seekBar == misguessesSeekBar)
    	{
    		//misguesses = progress;
    		setMisguessesPreference(progress);
    		Toast.makeText(SettingsActivity.this, "Amount of misguesses set to: " + progress, Toast.LENGTH_SHORT).show();
    	}
    }
    
    // Moves user back to previous page
	public void backToGame(View v){
		this.onBackPressed();
	}
	
	// Sets the amount of misguesses in internal storage
	public void setMisguessesPreference(int misguesses)
	{
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("misguesses", misguesses);
		editor.commit();
	}
	
	// sets the word lenght in internal storage
	public void setWordLengthPreference(int wordLength)
	{
		SharedPreferences sharedPref = this.getSharedPreferences(HANGMAN_VARIABLES, this.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt("wordLength", wordLength);
		editor.commit();
	}
}