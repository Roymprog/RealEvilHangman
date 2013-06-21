package nl.mprog.apps.EvilHangman6081282;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HighScore{

	public String TABLE_NAME = "HIGH_SCORES";
	
	public int lowestScore;
	public String highScoreWord;
	public int misguessesUsed;
	
	private SQLiteDatabase myDataBase;

	public DatabaseHelper dbhelper;
	
	public HighScore(DatabaseHelper dbhelper){
		this.dbhelper = dbhelper;
	}
	
	// opens the database and puts database in class variable
	private void openDatabase(){
		myDataBase = dbhelper.openDataBase();
	}

	// updates the high scores in the database 
	public void updateHighScores(int score, String hangmanWord, int usedGuesses, String gamePlay){
		if (amountOfHighScores() >= 10){
			if(scoreInDatabase(score)){
				deleteLowest();
				insertHighScore(score, hangmanWord, usedGuesses, gamePlay);
			}
			return;
		}
		else{
			insertHighScore(score, hangmanWord, usedGuesses, gamePlay);
		}
	}
	
	// adds new high score to the database
	private void insertHighScore(int score, String hangmanWord, int misguesses, String gamePlay){
		ContentValues cv = new ContentValues();
		String column = "hangman_word";
		cv.put(column, hangmanWord);
		column = "misguesses";
		cv.put(column, misguesses);
		column = "score";
		cv.put(column, score);
		column = "player";
		cv.put(column, gamePlay);
		myDataBase.insert(TABLE_NAME, null, cv);
	}
	
	// checks if score is high enough to be added to database
	private boolean scoreInDatabase(int score){
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES ORDER BY score ASC LIMIT 1", new String[] {});
		cur.moveToFirst();
		highScoreWord = cur.getString(2);
		misguessesUsed = cur.getInt(3);
		lowestScore = cur.getInt(4);
		return(score > lowestScore);
	}
	
	// counts the amount of high scores in database
	private int amountOfHighScores(){
		openDatabase();
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES", new String[] {});
		int count = cur.getCount();
		return count;
	}
	
	// deletes the lowest score from the database
	private void deleteLowest(){
		myDataBase.execSQL("DELETE FROM HIGH_SCORES WHERE score = "+ lowestScore +" AND misguesses = "+ misguessesUsed +"");
	}
	
	// gets all the scores from database ranked from highest to lowest
	public Cursor getHighScores(){
		myDataBase = dbhelper.openDataBase();
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES ORDER BY score DESC", new String[] {});  
		return cur;
	}
}
