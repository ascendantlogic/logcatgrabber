##LogcatGrabber
This is just a little project I wrote to see what logs an application would have access to without root access to a phone. As of Android 4.1 non-root applications only have access to their own app-level logs so the other 3 logs will be empty. However on 4.0 and below the system, radio and event logs reveal a wealth of information. I created this app to snapshot the logs each time you press the button so you could attempt to capture the results of certain behavior or events after they happen.

This code is pretty rough and something I did in about 20 minutes so don't knock me on style ;)

Relevant: [Android Logging](http://elinux.org/Android_Logging_System)