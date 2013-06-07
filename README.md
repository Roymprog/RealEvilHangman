Evil Hangman
===========

<h2>Summary:</h2>
This app lets you play the old game of hangman. The difference with hangman is that 'Evil Hangman' is designed to be unbeatable. 

<h2>Features:</h2>

- When the app is launched, a game should automatically start(except when a game was backgrounded)
- The word to be guessed should be displayed as hyphens at the place of the letters to be guessed
- The app will keep track of and display the amount of moves a user has left
- The app willdisplay all the letters that may be guessed
- The user will be able to input letters via an onscreen keyboard
- The app will only accept alphabetical characters(case insensetive)
- Invalid input will be handled with showing an error message, but not be penalized
- The app will have a title, logo and a new game button
- If the user has guessed the word before running out of moves, the user should be congratulated and no further input should be registered
- Else if the user ran out of moves before guessing the word, the game should be stopped and the user should start a new game via the menu
- Via the menu, the user should be able to change three options: 
  1. The word length between 1 and n(longest word in used dictionary)
  2. The maximum allowed number of misguesses
  3. The option whether the game should apply ´evil´ or ´normal´ gameplay
- The menu should be accessible when playing the game by hitting the menu button, not when displaying the high scores
- The app will have default values for the three settings; word length = 7, misguesses = 5 and gameplay = 'evil', set by PreferenceActivity
- THe word length and amount of misguesses will be adjustable with a slide controller and the gameplay will be controlled by a switch
- Settings should only be applied on new games
- The app must maintain high scores which will be showed anytime a game has been won
- High scores will be based on the three selectable options in the menu

<h2>Features:</h2>

- The app will be designed for smartphones(320p x 480p)
- The words used for the hangman words come from the file words.plist
- The game state will be stored in local storage. The user should be able to continue his/her game when the app was closed or the device was shut down.
- The display of the highscores will be in a separate 'activity' through a transition.
- Methods will be implemented to store and retrieve high scores. The high scores will be stored in SQLite library
- User guesses will be obtained via a text field, inputs should be confirmed
- The app will have two separate model classes for 'evil' and 'normal' gameplay, resp. EvilGameplay and GoodGameplay
- Unit tests will be developped for the models created
- The app has to work on the device I'm using

<h2>Mock-ups:</h2>
![Link to Mock-ups](https://github.com/Roymprog/EvilHangman/blob/master/doc/)
