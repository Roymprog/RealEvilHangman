package nl.mprog.apps.EvilHangman6081282;

public class OfflineHighScoresModel{

	public String TABLE_NAME = "HIGH_SCORES";

	public int lowestScore;
	public String highScoreWord;
	public int misguessesUsed;

	public DatabaseHelper dbhelper;

	public OfflineHighScoresModel(DatabaseHelper dbhelper){
		this.dbhelper = dbhelper;
	}

	/* updates the high scores in the database */ 
	public void updateHighScores(int score, String hangmanWord, int usedGuesses, String gamePlay){
		if (dbhelper.amountOfHighScores() >= 10){
			if(dbhelper.scoreInDatabase(score)){
				dbhelper.deleteLowestScoreFromDatabase(lowestScore, misguessesUsed);
				dbhelper.insertHighScore(score, hangmanWord, usedGuesses, gamePlay);
			}
			return;
		}
		else{
			dbhelper.insertHighScore(score, hangmanWord, usedGuesses, gamePlay);
		}
	}
}
