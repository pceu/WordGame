Title: "# Word Game" 

Github Link - https://github.com/pceu/WordGame 

Name: Phiar Ceu Hnin    Name: Nam Youn Kim
SID : 217281975          SID: 217429291

Overview
	The app, Word Game, as the name suggests, is about guessing and filling letters in the given empty boxes using
 the provided question as a hint. The app does not filter or targeted any particular demographic; hence, it can be played by anyone 
who is interested in broadening their knowledge in English language. This is because the difficulty of the game solely depends on user’s proficiency 
and familiarity in English, not with the user’s age. Currently, the app offers three levels with two different mode – with timer mode or without 
timer mode – which they differ in their level of difficulties. With timer mode, user is only allowed to complete the question with the given time frame while 
the other has no duration limit. To have more fun with the game, challenges do not last forever as users are supported with features that will completely skip 
the question or give a partial alphabet from the answer. Not to be too excited, using those features require user to earn coins from answering correctly and 
the coin are in turn used for skipping and receiving hint. Moreover, user can enjoy listening to music while playing the game, or just turn off the music if 
user thinks it’s distracting. Because of time limited we are unable to develop some other features; however, more enjoyable features will be added over time.

Explanation of Major Features
   # Level
	There are currently three different levels in this app and the main differences between them are the number of letters of the answer and the hardness
	to guess the word as some words in higher levels are words that are not used in our daily life. More and variety levels are planned to add in the future
	such as topical questions and so on. 
   # Coin
	Coin plays a critical role in the game as it helps user to get the answer quicker. Users are rewarded with coins when they get the answer correct, and they
	can use these either to get a hint (an alphabet from the answer) or completely skip the question.
   # Hint
	As explanied in the previous feature (coin), hint consumes 10 coins per use and throw an alphabet from the answer and fill in the box given automatically.
   # Skip
	Skipping the question needs more coins (30 coins) as it just brings the user to the next question page without needing to answer anything. 
   # Timer
	We cannot say it is reality when we are not controlled by time; too little of excitement, joyfulness and challenges are involved without time.
	Just because of this, ‘Timer’ is added and requires users to complete the answer within the time given on the screen. To use this feature, 
	user can go to setting and turn on or off the timer.
   # Resume user progress (storing and retrieving from Room)
	User progress during the game such as level number, question number and coin number are saved into local database using Room in Android. This allows user
	 to resume the question they are up to and save the coin number from the previous play.
   # Backgrouond Music
	Background music is added in the game and played by default, however, user can turn on or off from the setting page.



API Reference (major classes and methods)



