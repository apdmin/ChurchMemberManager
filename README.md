ChurchMemberManager
===================

Introduction
------------

ChurchMemberManager is designed as the front end to the PCBC church membership database.
It's purpose is to provide easy access to simple and common tasks within the PCBC MySQL Database.

Log Files
---------
When running this application, two log files will be generated in whatever directory you run the
application from. In most cases, this will be the same directory where the .jar file is located.


Installation
------------
Installing this application is simply a matter of placing "ChurchMemberManager.jar" somewhere on
your local hard drive. Note that wherever you place this file, will be the same location the two
log files show up. It is thus generally recommended to place "ChurchMemberManager.jar" in it's own
folder somewhere. If you download this repository as a .zip file, unzipping this file will generate
such a folder.


Running the Application
-----------------------
To run the application, you can simply double click on the "ChurchMemberManager.jar" file. If you
prefer to run it from the command line, you can navigate to the location of the jar file and then
type.
	java -jar ChurchMemberManager.jar


Troubleshooting
---------------
*"I accidentally entered the wrong information at the start screen and now I can't fix it."*
  Solution: Delete 'ConnectionInfo.ci' from the application directory. This will force the
            application to prompt you with the start screen again.

