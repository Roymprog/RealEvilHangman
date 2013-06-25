package nl.mprog.apps.EvilHangman6081282;

import java.util.List;

public interface GamePlayInterface {
	public String hangmanWord = null;
	public int misguesses = 0;

	public void playLetter(String input);

	public boolean hasWon();

	public boolean hasLost();

	public int getScore();

	public int getWordLength();

	public List<Character> getHangmanCharacterList();

	public List<Character> getLettersLeft();

	public int getMisguesses();

	public String getFinalWord();
}
