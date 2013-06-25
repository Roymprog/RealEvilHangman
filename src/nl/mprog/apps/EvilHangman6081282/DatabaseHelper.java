package nl.mprog.apps.EvilHangman6081282;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/nl.mprog.apps.EvilHangman6081282/databases/";
    private static String DB_NAME = "evilhangman.db";
 
    private SQLiteDatabase myDataBase; 

    public int wordsWithThisLength;
 
    private final Context myContext;
 
    // The constructor
    public DatabaseHelper(Context context) {
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
    // Creates the database if it does not exist
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
 
    // Checks if database is already in memory
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
 
    // copies the database from assets to the right location data/data/nl.mprog.apps.EvilHangman6081282/databases/
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

    // Opens the database for read and write purpose
    public SQLiteDatabase openDataBase() throws SQLException{
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	return myDataBase;
    }
    
    // closes the database
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
	
	// Gets a pseudorandom word of the given lenght from the database
	public String getDatabaseWord(int length){
		//SQLiteDatabase db = this.getReadableDatabase();
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
} 