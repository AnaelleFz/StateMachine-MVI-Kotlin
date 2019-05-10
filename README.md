# StateMachine-MVI-Kotlin
Android application. A simple state machine built in Kotlin with MVI architecture.

#### The different States :
- STOP -> INIT or ERROR
- INIT -> START
- START -> STOP or ALERT or ERROR
- ALERT -> START or ERROR
- ERROR -> STOP

With an Android Service which sends ALERT events randomly and ERROR events every minute.  
