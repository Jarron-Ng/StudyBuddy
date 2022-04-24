# 1D project - StudyBuddy
Our app created for ***50.001 Information system and Programming*** module. 

***StudyBuddy*** is targeted toward students to keep track of their work at hand and develop good study habits using a pomodoro timer.


As with all androidstudio projects, java scripts are located in app/src/main/java
We have a models folder that contains all our model objects used for recycler view and to create our own Task object.


### Files and their usage in /myapplication
- /models: Utility files to support recycler view and to specify our own Tasks Class/Object
- *AddTask.java*: Adds a task object to firestore and to explore.java
- *explore.java*: Main landing page upon login that displays all tasks, navigation to pomodoro timer, quiz
- *Level1, Level2.java*: Loads and display quizzes according to difficulty
- *Login.java*: An adapter that holds our fragment activity for the login/signup page
- *loginFragment.java*: Handles user logins and firebase authentication
- *QuizMenu*: Displays quiz menu
- *QuestionBank*: Utility class used to store and load questions for quiz
- *signupFragment.java*: Handles user signups and firebase storage
- *startup.java*: Start up page when app is first pressed
- *Timer.java*: Pomodoro timer for user to use it as a stopwatch to keep track of studying time


#### Contributed by:

Jon-Taylor Lim (1005053)
Ng Jing Heng Jarron (1005548)
Ng Jowie (1005494)
Royden Yang (1005219)
Eunice Chua (1005546)
Ang Yu Jie (1005270)
Nguyen Thai Huy (1005374)

***SUTD 50.001***
