# Fnf-Location-Tracker
Android app to track your friends and family current location.
![alt text](https://i.ibb.co/ZVQTXby/Screenshot-2019-10-19-12-01-03-234-xyz-mmhasanovee-fnflocationtracker.png )


## Demo/Play Store link
* [Fnf Location Tracker](https://play.google.com/store/apps/details?id=xyz.mmhasanovee.fnflocationtracker)

## Requirements
*Android Studio

## Install
1. Connect Firebase Realtime database to the project. Download the Realtime Database project's google-services.json file. And copy
and paste the json data on **Fnf-Location-Tracker-Master/App/google-services.json file.**
2. Connect with your Google Maps api from Google Cloud Console. Copy the Android api key and add it to 
**Fnf-Location-Tracker-Master/App/debug/res/values/google_maps_api.xml** file's **Your-Key-Here position**. Replace the Your-Key-Here by
Google Maps Android Api key.
3. Connect with Firebase Cloud Messaging. Go to your Firebase **project>Project Setting>Cloud Messaging>Copy Server key** and paste it to   **Fnf-Location-Tracker/app/src/main/java/xyz/mmhasanovee/fnflocationtracker/Remote/IFCMService.java** file's  **"Authorization:key="** field. Suppose, server key = xyzmariariariaria. So, **"Authorization:key=xyzmariariariaria"**.
3. Run the project. It will download the Gradle/other files itself.

## Features

1. Google login
2. User profile update
3. Search new people
4. Add/remove friends
5. Locate friends
6. Locate your position



## License

``` 

```
