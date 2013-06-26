package nl.mprog.apps.EvilHangman6081282;

import java.util.ArrayList;
import java.util.List;

public class GoodGamePlay implements GamePlayInterface{

	public final static String HANGMAN_VARIABLES = "nl.mprog.apps.EvilHangman6081282.HANGMAN_VARIABLES";
	public final static String HIGH_SCORES = "nl.mprog.apps.EvilHangman6081282.HIGH_SCORES";

	public int totalMisguesses;
	public int misguesses;
	public int hangmanWordLength;
	public int wordsInLibraryWithLength;
	public String hangmanInput;
	public String hangmanWord;
	public static List<Character> lettersLeft = new ArrayList<Character>();
	public static List<Character> hangmanCharacterList = new ArrayList<Character>();

	public DatabaseHelper dbhelper;
	public OfflineHighScoresModel offlineHighScoresModel = new OfflineHighScoresModel(dbhelper);

	public GoodGamePlay(int misguesses, String hangmanWord, int wordsInLibraryWithLength, DatabaseHelper dbhelper) {
		this.dbhelper = dbhelper;
		this.misguesses = this.totalMisguesses = misguesses;
		this.hangmanWord = hangmanWord;
		this.wordsInLibraryWithLength = wordsInLibraryWithLength;
		this.hangmanWordLength = hangmanWord.length();
		setSettings();
	}

	/* handles all that involves playing a letter */
	public void playLetter(String input){
		int length = input.length();
		if (length == 1) {
			char upperCaseLetter = Character.toUpperCase(input.charAt(0));
			if (Character.isLetter(upperCaseLetter) && alreadyPlayed(upperCaseLetter) == false){
				List<Integer> indices = new ArrayList<Integer>();
				indices = findIndices(upperCaseLetter, hangmanWord);
				if (indices.size() > 0){
					changeHangmanWord(indices, upperCaseLetter);
				}
				else{
					misguesses--;
				}
			}
		}
	}

	/* sets the class variables for the hangman game that is played */
	public void setSettings(){
		hangmanCharacterList = setHangmanCharacterArray();
		setLettersLeft();
	}

	/* Looks for the indices of the played character in the hangman word */
	public List<Integer> findIndices(char character, String hangmanWord){
		List<Integer> indices = new ArrayList<Integer>();
		int start = 0;
		while(hangmanWord.indexOf(character, start) >= 0){
			indices.add(hangmanWord.indexOf(character, start));
			start = hangmanWord.indexOf(character, start) + 1;
		}
		return indices;
	}

	/* sets the new display after playing a letter */
	public void changeHangmanWord(List<Integer> indices, char character){
		for (int i : indices){
			hangmanCharacterList.set(i, character);
		}
	}

	/* sets the initial hangman word(all underscores) */
	public List<Character> setHangmanCharacterArray(){
		hangmanCharacterList.clear();
		for (int i = 0; i < hangmanWordLength; i++){
			hangmanCharacterList.add('_');
		}
		return hangmanCharacterList;
	}

	/*  sets the initial letters that may be played(A - Z) */
	public void setLettersLeft(){
		char next = 'A';
		lettersLeft.clear();
		for (int i = 0; i < 26; i++){
			lettersLeft.add(next);
			int charValue = next;
			next = (char) (charValue + 1);
		}
	}

	/* gets class variables */ 
	public int getWordLength(){
		return hangmanWordLength;
	}

	/* returns the individual hangman characters */
	public List<Character> getHangmanCharacterList(){
		return hangmanCharacterList;
	}

	/* returns the individual letters left */
	public List<Character> getLettersLeft(){
		return lettersLeft;
	}

	/* returns the amount of misguesses left */
	public int getMisguesses(){
		return misguesses;
	}

	/* returns the amount of misguesses left */
	public int getTotalMisguesses(){
		return totalMisguesses;
	}
	
	/* checks if a letter has already been played */
	public boolean alreadyPlayed(char letterPlayed){
		int spot;
		spot = lettersLeft.indexOf(letterPlayed);
		if(spot >= 0){
			lettersLeft.set(spot, '_');
			return false;
		}
		return true;
	}

	/* checks for a won game */
	public boolean hasWon(){
		for(char c : hangmanCharacterList){
			if (c == '_'){
				return false;
			}
		}
		return true;
	}

	/* checks for a lost game */
	public boolean hasLost(){
		return (misguesses <= 0);
	}

	/*  calculates score */
	public int getScore(){
		int score;
		int usedGuesses = totalMisguesses - misguesses;
		int maxWordLength = 24;
		int maxAmountThatGetsScore = 25;
		if (totalMisguesses != 26){
			score = wordsInLibraryWithLength / totalMisguesses + (maxWordLength - hangmanWordLength) * (maxAmountThatGetsScore - usedGuesses);
		}
		else{
			score = 0;
		}
		return score;
	}

	/* returns the guessed word */
	public String getFinalWord(){
		return hangmanWord;
	}
}