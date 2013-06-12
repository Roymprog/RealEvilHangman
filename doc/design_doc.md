Evil Hangman
===========

<h2>Summary:</h2>
This app lets you play the old game of hangman. The difference with hangman is that 'Evil Hangman' is designed to be unbeatable. 

<h2>Dislay features:</h2>

- When the app is launched, a game should automatically start
- The word to be guessed should be displayed as hyphens at the place of the letters to be guessed
- The app will keep track of and display the amount of moves a user has left
- The app will display all the letters that may be guessed
- Invalid input will be handled with showing an error message, but not be penalized
- The app will have a title, logo and a new game button
- If the user has guessed the word before running out of moves, the user should be congratulated and no further input should be registered
- The app must maintain high scores which will be showed anytime a game has been won
- The menu should be accessible when playing the game by hitting the menu button, not when displaying the high scores
- While the amount of misguesses left decreases a visualization will be displayed. The display shows a man getting hanged, increasing its limbs

<h2>Gameplay features:</h2>

- The app will only accept alphabetical characters(case insensetive)
- Via the menu, the user should be able to change three options: 
  1. The word length between 1 and n(longest word in used dictionary)
  2. The maximum allowed number of misguesses
  3. The option whether the game should apply ´evil´ or ´normal´ gameplay
- Else if the user ran out of moves before guessing the word, the game should be stopped and the user should start a new game via the menu
- The user will be able to input letters via the onscreen keyboard
- The word length and amount of misguesses will be adjustable with a slide controller and the gameplay will be controlled by a switch
- The app will have default values for the three settings; word length = 7, misguesses = 6 and gameplay = 'evil', set in sharedPreferences with default values 6, 7 and 'evil'
- Settings should only be applied on new games
- High scores will be based on the three selectable options in the menu

<h2>Technical requirements: </h2>

- The app will be designed for smartphones(320p x 480p)
- The words used for the hangman words come from the file words.plist
- The game state will be stored in local storage. The user should be able to continue his/her game when the app was closed or the device was shut down.
- The display of the highscores will be in a separate 'activity' through a transition.
- Methods will be implemented to store and retrieve high scores
- The high scores will be stored on a ruby-scaffolded highscore-server
- User guesses will be obtained via a text field, inputs should be confirmed
- The app will support two classes for the gameplay, one for 'evil' and one for 'normal' gameplay
- A 'gameplay delegate' will be used as interface for the app to communicate with either one of the gameplays
- Unit tests will be developped for the models created
- The app has to work on the device I'm using

<h3>Databases:</h3>

High score ruby scaffolded MySQL server containing:

Scaffold contains:
- Evil/Good gameplay
- Played hangman word 
- Used guesses 
- Score
 
Retrieve information from database with httpGetRequest as JSON or XML
Put information in database with httpPost

(Not sure how I want to implement it)


SQlite database for words

Table[wordlength]:

Word(String)


<h3>Classes</h3>
- MainActivity
- DisplayVisualization(fragment)
- HighScoresActivity
- SettingsActivity
- DatabaseHelper
- HighScore
- GamePlayInterface (actually an interface)
- GoodGamePlay --> implements GamePlayInterface
- EvilGamePlay --> implements GamePlayInterface

<h3>New methods not present in normal hangman:</h3>

- List <String> getHangmanWordList (int wordLength)
  -  Method gets all words with a defined length and returns them as a list

- List <String> updateList (char c, List <String> wordList)
  - The list retreived by getHangmanWordList will be updated
  - All words with the played letter at the same index will be stored in a map
  - The key will be an array of integers containing the indices of the played letter
  - The value will be a list of all the strings with the same indices
  - A counter with the same name as the indices will be increased every time a word is added to the corresponding list
  (int 0, will count all the words with no index, int 13 will count all with indices 1 and 3 and 258 will count words with indices 2, 5 and 8)
  - The list with the most words will be returned 


<h2>Mock-ups:</h2>
![Link to Mock-ups](https://github.com/Roymprog/EvilHangman/blob/master/doc/)

(another slider control will be implemented for the Evil/Normal gameplay handling)
