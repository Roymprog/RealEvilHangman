package nl.mprog.apps.EvilHangman6081282;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/nl.mprog.apps.EvilHangman6081282/databases/";
	private static String DB_NAME = "evilhangman.db";
	public String TABLE_NAME = "HIGH_SCORES";
	
	private SQLiteDatabase myDataBase; 

	public int wordsWithThisLength;

	private final Context myContext;

	 /*The constructor*/
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}	

	/* Creates the database if it does not exist*/
	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(!dbExist){
			//this method creates an empty database at the selected path
			this.getReadableDatabase();
			try {
				copyDataBase();
			}catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/*Checks if database is already in memory*/
	private boolean checkDataBase(){

		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		}catch(SQLiteException e){

		}
		if(checkDB != null){

			checkDB.close();

		}
		return (checkDB != null);
	}

	/* copies the database from assets to the right location data/data/nl.mprog.apps.EvilHangman6081282/databases/*/
	private void copyDataBase() throws IOException{

		InputStream myInput = myContext.getAssets().open(DB_NAME);

		String outFileName = DB_PATH + DB_NAME;

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/*Opens the database for read and write purpose*/
	public SQLiteDatabase openDataBase() throws SQLException{
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;
	}

	/* closes the database*/
	@Override
	public synchronized void close() {
		if(myDataBase != null){
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// onCreate will not be used, but must be implemented
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// onUpgrade will not be used, but must be implemented
	}

	/*Gets a pseudorandom word of the given lenght from the database*/
	public String getDatabaseWord(int length){
		openDataBase();
		Cursor cur = myDataBase.rawQuery("SELECT _id"+length+" as _id,word"
				+length+" from words_length_"+length,new String [] {});
		int count = cur.getCount();
		MainActivity.wordsInLibraryWithLength = count;
		int random = new Random().nextInt(count);
		cur.move(random - 1);
		String string = cur.getString(1);
		return string;
	}

	/* This function returns the complete list of words with the selected word length */
	public List<String> getWordList(int length){
		openDataBase();
		Cursor cur = myDataBase.rawQuery("SELECT _id"+length+" as _id,word"
				+length+" from words_length_"+length,new String [] {});
		int count = cur.getCount();
		int i = 0;
		List<String> wordlist = new ArrayList<String>(); 
		while (i < count)
		{
			cur.moveToPosition(i);
			wordlist.add(cur.getString(1));
			i++;
		}
		return wordlist;
	}
	
	/* adds new high score to the database */
	public void insertHighScore(int score, String hangmanWord, int misguesses, String gamePlay){
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
	
	/* checks if score is high enough to be added to database */
	public boolean scoreInDatabase(int score){
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES ORDER BY score ASC LIMIT 1", new String[] {});
		cur.moveToFirst();
		int lowestScore = cur.getInt(4);
		return(score > lowestScore);
	}
	
	/* counts the amount of high scores in database */
	public int amountOfHighScores(){
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES", new String[] {});
		int count = cur.getCount();
		return count;
	}
	
	/* deletes the lowest score from the database */
	public void deleteLowestScoreFromDatabase(int lowestScore, int misguessesUsed){
		myDataBase.execSQL("DELETE FROM HIGH_SCORES WHERE score = "+ lowestScore +" AND misguesses = "+ misguessesUsed +"");
	}
	
	/* gets all the scores from database ranked from highest to lowest */
	public Cursor getHighScores(){
		openDataBase();
		Cursor cur = myDataBase.rawQuery("SELECT * FROM HIGH_SCORES ORDER BY score DESC", new String[] {});
		return cur;
	}
} 