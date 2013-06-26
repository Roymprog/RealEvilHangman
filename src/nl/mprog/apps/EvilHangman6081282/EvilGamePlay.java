package nl.mprog.apps.EvilHangman6081282;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvilGamePlay implements GamePlayInterface{

	public final static String HANGMAN_VARIABLES = "nl.mprog.apps.EvilHangman6081282.HANGMAN_VARIABLES";
	public final static String HIGH_SCORES = "nl.mprog.apps.EvilHangman6081282.HIGH_SCORES";

	public int totalMisguesses;
	public int misguesses;
	public int hangmanWordLength;
	public int wordsInLibraryWithLength;
	public String hangmanInput;
	public String hangmanWord;
	public List<String> hangmanWordList;
	public static List<Character> lettersLeft = new ArrayList<Character>();
	public static List<Character> hangmanCharacterList = new ArrayList<Character>();

	public DatabaseHelper dbhelper;
	public OfflineHighScoresModel offlineHighScoresModel = new OfflineHighScoresModel(dbhelper);

	public EvilGamePlay(int misguesses, int wordsInLibraryWithLength, int hangmanWordLength, List<String> wordList, DatabaseHelper dbhelper) {
		this.dbhelper = dbhelper;
		this.misguesses = totalMisguesses = misguesses;
		this.hangmanWordList = wordList;
		this.hangmanWordLength = hangmanWordLength;
		this.wordsInLibraryWithLength = wordsInLibraryWithLength;
		setSettings();
	}

	/* updates the list of words that are still possible with the
	 * letters that have been played */
	public List<Integer> updateHangmanWordList(char playedLetter){
		List <Integer> indices = new ArrayList<Integer>();
		List <String> wordList = new ArrayList<String>();
		List <List<Integer>> listOfListWithIndices = new ArrayList<List<Integer>>();
		HashMap<List<Integer>, List<String>> wordListMap = new HashMap<List<Integer>, List<String>>();

		// puts all words with the played letter at the same index in a list
		sortWordsWithSameIndices(indices, wordList, listOfListWithIndices, wordListMap, playedLetter);

		// returns the indices of the largest group of words, also replaces handmanWordList with the new list
		indices = getLongestListOfWords(indices, wordList, listOfListWithIndices, wordListMap);
		return indices;
	}
	
	/* Puts all the words with the played letters at the same indices
	 * in a sorted list */
	public void sortWordsWithSameIndices(List<Integer> indices, 
			List<String> wordList, 
			List<List<Integer>> listOfListWithIndices, 
			HashMap<List<Integer>, List<String>> wordListMap,
			char playedLetter){
		for (String word : hangmanWordList){
			indices = findIndices(playedLetter, word);
			if(wordListMap.containsKey(indices))
			{
				wordList = new ArrayList<String>();
				wordList = wordListMap.get(indices);
				wordList.add(word);
				wordListMap.put(indices, wordList);
			}
			else{
				wordList = new ArrayList<String>();
				wordList.add(word);
				wordListMap.put(indices, wordList);
				listOfListWithIndices.add(indices);
			}
		}
	}

	/* Looks through all the sorted lists and picks the list with the biggest amount 
	 * of words in it. If 2 lists are of the same length, the list with the played letter
	 * at the least amount of indices is selected */
	public List<Integer> getLongestListOfWords(List<Integer> indices, 
			List<String> wordList, 
			List<List<Integer>> listOfListWithIndices, 
			HashMap<List<Integer>, List<String>> wordListMap){
		List <String> longestList = new ArrayList<String>();
		int longest = 0;
		for (List<Integer> list : listOfListWithIndices){
			wordList = wordListMap.get(list);
			if(wordList.size() > longest){
				indices = list;
				longestList = wordList;
				longest = wordList.size();
			}
			else if (wordList.size() == longest)
			{
				if(indices.size() > list.size())
				{
					indices = list;
					longestList = wordList;
					longest = wordList.size();
				}
			}
		}
		hangmanWordList = new ArrayList<String>();
		hangmanWordList = longestList;
		return indices;
	}

	/*( handles all that involves playing a letter */
	public void playLetter(String input){
		int length = input.length();
		if (length == 1) {
			char upperCaseLetter = Character.toUpperCase(input.charAt(0));
			if (Character.isLetter(upperCaseLetter) && alreadyPlayed(upperCaseLetter) == false){
				List<Integer> indices = new ArrayList<Integer>();
				indices = updateHangmanWordList(upperCaseLetter);
				if (indices.size() > 0){
					changeHangmanWord(indices, upperCaseLetter);
				}
				else{
					misguesses--;
				}
			}
		}
	}

	// sets the class variables for the hangman game that is played
	public void setSettings(){
		hangmanCharacterList = setHangmanCharacterArray();
		setLettersLeft();
	}

	// Looks for the indices of the played character in the hangman word
	public List<Integer> findIndices(char character, String hangmanWord){
		List<Integer> indices = new ArrayList<Integer>();
		int start = 0;
		int index;
		while((index = hangmanWord.indexOf(character, start)) >= 0){
			indices.add(index);
			start = index + 1;
		}
		return indices;
	}

	/* sets the new display after playing a letter */
	public void changeHangmanWord(List<Integer> indices, char character){
		for (int i : indices){
			hangmanCharacterList.set(i, character);
		}
	}

	/* sets the initial hangman word(all underscores)*/
	public List<Character> setHangmanCharacterArray(){
		hangmanCharacterList.clear();
		for (int i = 0; i < hangmanWordLength; i++){
			hangmanCharacterList.add('_');
		}
		return hangmanCharacterList;
	}

	/* sets the initial letters that may be played(A - Z)*/
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
		boolean played = true;
		int spot;
		spot = lettersLeft.indexOf(letterPlayed);
		if(spot >= 0){
			lettersLeft.set(spot, '_');
			played = false;
		}
		return played;
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

	/* checks for a lost game*/
	public boolean hasLost(){
		return (misguesses <= 0);
	}

	/* calculates score */
	public int getScore(){
		int score;
		int usedGuesses = totalMisguesses - misguesses;
		int maxWordLength = 24;
		if (totalMisguesses != 26){
			score = wordsInLibraryWithLength * (1 / (26 - totalMisguesses)) - usedGuesses + (maxWordLength - hangmanWordLength);
		}
		else{
			score = 0;
		}
		return score;
	}

	/* puts all good-guessed letters together to form the guessed word */
	public String getFinalWord(){
		StringBuilder stringBuilder = new StringBuilder("");
		for (char c : hangmanCharacterList){
			stringBuilder.append(c);
		}
		return stringBuilder.toString();	
	}
}