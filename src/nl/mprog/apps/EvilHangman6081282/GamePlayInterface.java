package nl.mprog.apps.EvilHangman6081282;

import java.util.List;

public interface GamePlayInterface {

	public void playLetter(String input);

	public boolean hasWon();

	public boolean hasLost();

	public int getScore();

	public int getWordLength();

	public List<Character> getHangmanCharacterList();

	public List<Character> getLettersLeft();

	public int getMisguesses();

	public int getTotalMisguesses();
	
	public String getFinalWord();
}
