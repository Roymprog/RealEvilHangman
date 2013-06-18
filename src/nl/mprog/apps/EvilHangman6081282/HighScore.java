package nl.mprog.apps.EvilHangman6081282;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class HighScore extends Activity {

	public String HIGH_SCORES = "HIGH_SCORES";
	
	public int lowestScore;
	public String highScoreWord;
	public int misguessesUsed;
	
	public DatabaseHelper dbhelper = new DatabaseHelper(this);
	
	private SQLiteDatabase myDataBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// opens the database and puts database in class variable
	public void openDatabase(){
		myDataBase = dbhelper.openDataBase();
	}

	// closes the database
	public void closeDatabase(){
		//dbhelper.close();
	}
	
	// updates the high scores in the database 
	public void updateHighScores(int score, String hangmanWord, int usedGuesses){
		if (amountOfHighScores() >= 10)
		{
			if(scoreInDatabase(score)){
				deleteLowest();
				insertHighScore(score, hangmanWord, usedGuesses);
			}
			return;
		}
		else
		{
			insertHighScore(score, hangmanWord, usedGuesses);
		}
	}
	
	// adds new high score to the database
	public void insertHighScore(int score, String hangmanWord, int misguesses){
		ContentValues cv = new ContentValues();
		String table = HIGH_SCORES;
		String column = "hangman_word";
		cv.put(column, hangmanWord);
		column = "misguesses";
		cv.put(column, misguesses);
		column = "score";
		cv.put(column, score);
		myDataBase.insert(table, null, cv);
	}
	
	// checks if score is high enough to be added to database
	public boolean scoreInDatabase(int score){
		boolean bool;
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES ORDER BY score ASC LIMIT 1", new String[] {});
		cur.moveToFirst();
		highScoreWord = cur.getString(2);
		misguessesUsed = cur.getInt(3);
		lowestScore = cur.getInt(4);
		if (score > lowestScore)
		{
			bool = true;
		}
		else 
		{
			bool = false;
		}
		return bool;
	}
	
	// counts the amount of high scores in database
	public int amountOfHighScores(){
		openDatabase();
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES", new String[] {});
		int count = cur.getCount();
		return count;
	}
	
	// deletes the lowest score from the database
	public void deleteLowest(){
		myDataBase.execSQL("DELETE FROM HIGH_SCORES WHERE score = "+ lowestScore +" AND misguesses = "+ misguessesUsed +"");
		// AND hangmanWord = "+ highScoreWord +" AND misguesses = "+ misguessesUsed +"
	}
	
	// gets all the scores from database ranked from highest to lowest
	public Cursor getHighScores(){
		myDataBase = dbhelper.openDataBase();
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES ORDER BY score DESC", new String[] {});  
		return cur;
	}
	
}
