package nl.mprog.apps.EvilHangman6081282;

import java.util.ArrayList;
import java.util.List;

public interface GamePlayInterface {
	public void playLetter(String input);

	public void setSettings();

	public List<Integer> findIndices(char character);
	
	public void changeHangmanWord(List<Integer> indices, char character);
	
	public List<Character> setHangmanCharacterArray();
	
	public void setLettersLeft();
	
	public boolean alreadyPlayed(char letterPlayed);
	
	public boolean hasWon();
	
	public boolean hasLost();
	
	public int getScore();

	public int getWordLength();
	
	public List<Character> getHangmanCharacterList();
	
	public List<Character> getLettersLeft();
	
	public int getMisguesses();
}
