# Title: Word Game

Github Link - https://github.com/pceu/WordGame 

Name: Phiar Ceu Hnin    SID : 217281975 

Name: Nam Youn Kim	SID: 217429291

# Overview
Word Game, as the name suggests, is about guessing and filling letters in the given empty boxes using
the provided question as a hint. The app does not filter or target any particular demographic; hence, it can be played by anyone 
who is interested in broadening their knowledge in English language. This is because the difficulty of the game solely depends on user’s proficiency 
and familiarity in English, not with their age. Currently, the app offers three levels with two different mode – with timer mode or without 
timer mode – which they differ in their level of difficulties. With timer mode, user is only allowed to complete the question with the given time frame while 
the other has no duration limit. To have more fun with the game, challenges do not last forever as users are supported with features that will completely skip 
the question or give a partial alphabet from the answer. Not to be too excited, using those features require user to earn coins from answering correctly and 
the coin are in turn used for skipping and receiving hint. Moreover, user can enjoy listening to music while playing the game, or just turn off the music if 
user thinks it’s distracting. Because of time limited we are unable to develop some other features; however, more enjoyable features will be added over time.

# Explanation of Major Features

  - As we only have limited time for project video, we list major features here and some include features that we could not mention in the video.
   - Level: 
	There are currently three different levels in this app and the main differences between them are the number of letters of the answer and the hardness
	to guess the word as some words in higher levels are words that are not used in our daily life. More and variety levels are planned to add in the future
	such as topical questions and so on. 
   - Coin: 
	Coin plays a critical role in the game as it helps user to get the answer quicker. Users are rewarded with coins when they get the answer correct, and they
	can use these either to get a hint (an alphabet from the answer) or completely skip the question.
   - Hint: 
	As explanied in the previous feature (coin), hint consumes 10 coins per use and throw an alphabet from the answer and fill in the box given automatically.
   - Skip: 
	Skipping the question needs more coins (30 coins) as it just brings the user to the next question page without needing to answer anything. 
   - Timer: 
	We cannot say it is reality when we are not controlled by time; too little of excitement, joyfulness and challenges are involved without time.
	Just because of this, ‘Timer’ is added and requires users to complete the answer within the time given on the screen. To use this feature, 
	user can go to setting and turn on or off the timer.
   - Resume user progress (storing and retrieving from Room): 
	User progress during the game such as level number, question number and coin number are saved into local database using Room in Android. This allows user
	 to resume the question they are up to and save the coin number from the previous play.
   - Backgrouond Music: 
	Background music is added in the game and played by default, however, user can turn on or off from the setting page.
   - Erase Answer: 
	The word game allows user to click the answer button and put them back in the place they are before which really efficient for user as some does not allow
	user to erase and put back the letter.
   - Resuming game when screen rotate: 
	User want to resume their game when the screen change from portrait to landscape and vice versa. This app also allows user to continue/resume the game
	when configuration is changed. This include resuming the timer when changing from portrait to landscape.
   - Pause timer and release music when app is not active: 
	It is annoying to continue the timer in the game when user just switch to other app while playing games. This app pause timer and release the music and resume
	once user switch back to the game.
   - Keeping Errors in Log page: 
	This is one of the HD task for this project to present errors and bugs to user in a log page. We try and catch any error and saved them in the log activity so that user
	can view them. Once user does not need them anymore, user can delete the list view with just one press.

# Main Project File & Instruction)

	ChooseLevelActivity: WordGame\app\src\main\java\com\example\wordgame
	Level: WordGame\app\src\main\java\com\example\wordgame
	LevelData: WordGame\app\src\main\java\com\example\wordgame
	LevelOneActivity: WordGame\app\src\main\java\com\example\wordgame (including 2 and three)
	LogActivity: WordGame\app\src\main\java\com\example\wordgame
	MainActivity: WordGame\app\src\main\java\com\example\wordgame
	SettingActivity: WordGame\app\src\main\java\com\example\wordgame
	SplashScreen: WordGame\app\src\main\java\com\example\wordgame
	User: WordGame\app\src\main\java\com\example\wordgame
	UserDao: WordGame\app\src\main\java\com\example\wordgame
	UserDatabase: WordGame\app\src\main\java\com\example\wordgame
	Drawable: WordGame\app\src\main\res\drawable
	raw: WordGame\app\src\main\res\raw
	Instruction: Using android studio the project can be retrieved from GitHub (stated in the top of this document) and clone the project into the local drive.
		     Then, it can be easily compile and run the project using Android Studio.

# Project Main Directory Overview
 	Java -> com.example.wordgame: this directory contains the most important file for the project as they handle all the objects and materials (images, buttons ...)
		in the game. The most important one in this directory are:
		
		Level: This is a super class for all the levels (1, 2 and 3) and handles all almost all the operations from this class as all the levels inherits from 
		       this class. It initialize button, images and so on to be assigned in the child class. It also has crutial methods such as validating answer,
		       skipping, giving hint, setting timer and so on. Therefore, as mentioned in 'explanation for major features', this class plays an important role
		       for level 1, level 2 and level 3. Level 1, 2 and 3 will not be explained as they are all inherit from this class, thus sharing the properties
			of this class. Further explanation in the comments in the class.
		
		Log Activity: Has an attribute as a static list of string which is added whenever exception or bugs are thrown from java files in the project.
		
		Main Activity: If no data in the database is added, then assigned pre-hardcoded user data (coins number, level number...) into the database. Navigate user
			       to other page such as Level, log and setting.
		
		Setting Activity: Includes and handles code for setting timer and music to be on/off.

		User: Use for creating User object which are to be stored in the Room database. It contains properties such as table name, column name and so on for Room database.
		      This is also an entity class for ROOM.
		
		UserDao: This class contains methods needed for executing database commands and query for the Room database. 
		
		UserDatabase: Instantiate database instance if database is null or destroy database when destroyInstance is called.

	Drawable: contains vector assests, images, xml file for buttons shape and background colour.
	
	Layout: XML layout files for all the activity such as main, levels, log and so on. 

	Raw: Files such as csv file type and music file (mp3 in our case) are stored in this directory. CSV file contains Level question number, question, answer and so on.
	     
# Advanced Features
	- Room Database: apply in Level (including Level 1, 2 and 3)
	- Input Stream & Buffer Reader (reading data from raw file in Android): apply in Level class (including Level 1, 2 and 3)
	- Inheritance in Java: apply in Level class
	- Saving State (resuming game in change of configuration): apply in Level class (including Level 1, 2 and 3)









