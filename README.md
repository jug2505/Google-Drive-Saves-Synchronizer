# Google-Drive-Saves-Synchronizer
This app was made to provide saves synchronization of PC games. It allows gamers to use one save for several computers and make saves backups like using Steam Cloud, Origin Cloud, etc. App uses Google Drive API to store game saves, so you can access your saves any time.

Make sure you have [Java 17](https://www.oracle.com/java/technologies/downloads/) installed

---

# Use
To use this app you can download the latest release from [release section](https://github.com/jug2505/Google-Drive-Saves-Synchronyzer/releases) and do this steps:
1. Open file ``SavesDownload`` and ``SavesUpload`` in notepad.
2. In last line ``java -jar Synchronizer-1.0.jar "Absolute path to the game saves folder" "download"`` change ``Absolute path to the game saves folder`` to save folder path of the game you want to synchronize. For example, for Dark Souls Remastered it is ``C:\Users\{user}\Documents\NBGI\DARK SOULS REMASTERED``
3. Save the files.
4. You also need to create folder named ``Saves`` on your Google Drive.

Now you can execute coresponding file. During the first start you need to add access to your Google Drive.

---

# Build
If you want your own independent instance of synchronizer, you need to do these steps:
1. Clone this repo or download source code.
2. Enable Google Cloud Platform project with the API. To create a project and enable an API, refer to [Create a project and enable the API](https://developers.google.com/workspace/guides/create-project) guide.
3. Authorize credentials for a desktop application. To learn how to create credentials for a desktop application, refer to [Create credentials](https://developers.google.com/workspace/guides/create-credentials).
4. Download OAuth 2.0 Client ID credentials from your Google Cloud application.
5. Rename credentials file to ``credentials.json`` and copy it to ``\src\main\resources`` folder.
6. Build project with executing ``./gradlew build`` terminal command in the project folder.
7. Now you can use JAR from ``\build\libs`` folder with command line parameters: first parameter - path to save folder; second - "upload" or "download". But advise to make bat files that will ask user for administrator privileges download batfile examples from. For this you need to change last line of files ``SavesDownload.bat`` and ``SavesUpload.bat`` and type instead of ``Absolute path to the game saves folder`` your game folder path and instead of ``Synchronizer-1.0.jar`` name of your jar file.
