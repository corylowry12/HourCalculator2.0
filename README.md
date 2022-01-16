# Hour Calculator
 App for calculating hours

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/K3K64AQVM)

# Status
[![GitHub issues](https://img.shields.io/github/issues/corylowry12/HourCalculator2.0)](https://github.com/corylowry12/HourCalculator2.0/issues)

[<img src="resources/img/google-play-badge.png" height="50">](https://play.google.com/store/apps/details?id=org.jitsi.meet)

Use this app to take input time, out time time and/or break time, to display your time in a decimal format, rounded to the second decimal place.

This app will also store the hours in a SQLite database, as well as let you view it and calculate total pay, and how many hours are stored, and the total hours of all the hours combined.

It also has various customization options to make the app suit your needs. 

I created this app in order to help me calculate my hours at work.

# Screenshots
<p float="left">
<img src=/Screenshots/screenshot1.png width="200" height="400"/>
<img src=/Screenshots/screenshot2.png width="200" height="400"/>
<img src=/Screenshots/screenshot3.png width="200" height="400"/>
<img src=/Screenshots/screenshot4.png width="200" height="400"/>
<img src=/Screenshots/screenshot5.png width="200" height="400"/>
<img src=/Screenshots/screenshot6.png width="200" height="400"/>
</p>

# Whats New in Version 9.0.6

# New Features

* Added ability to set the theme when follow system theme is chosen, to black or gray, whichever you choose

# Enhancements

* Added dialog to notify you if update is available

# Whats New in Version 9.0.5

# Bug Fixes

* Fixed issue with dot in the nav bar on the settings icon not disappearing after you view patch notes
* Fixed issue with build number having the wrong number in the version info view
* Fixed issue with nav bar being white no matter the theme selected, will now show up as black all the time unless colored nav bar is enabled
* Fixed issue with crashing if you entered anything other than numbers in the break text box

# Enhancements

* Tweaked background color of dialogs while using the system accent color

# Whats New in Version 9.0.4

# Bug Fixes
* Fixed issue with navigation bar not working in some activities
* Fixed issue with slight flickering when switching views
* Fixed issue with history being stored even when history was disabled
* Fixed issue with snack bar messages showing over the bottom navigation bar
* Fixed issue with history not sorting properly when sorting by date
* Fixed issue with in time and out time sorting the time as AM whenever the hour was equal to 12 PM
* Fixed issue with app saying you couldnt store time depending on the time inputted

# New Features

* Completely redesigned the app
* Added the ability to undo hour deletion
* Added the ability to delete hours from within the edit view
* Added new dot and image to tell you when you should go read the Patch Notes
* Added the ability to set the background color as a dark gray
* Added the ability to set theme to Follow System for light or dark theme
* Added break time functionality
* New FAQ section in case you have questions
* Added Version Info section to the app
* Added compliance for Google's new Material 3
* Added collapsing tool bars in the settings portion of the app that wil lcollapse when you scroll
* Added option to set navigation bar to match current accent color
* Added the ability to edit the date for each entry in history by clicking the date chip in the Edit View
* Added menu in each settings page to reset settings within that page to defaults

# Enhancements

* Improved theming for dialogs, now the background color of the dialog will be the chosen accent color
* The cars throughout the app will now be the color of the chosen accent color
* Info about stored hours will now show in a dialog instead of text at the bottom of the view
* Sorting methods will now show up in a dialog with radio buttons instead of a menu
* New animations when switching between views
* Renamed Layout Settings to App Settings
* Improved vibration 
* Improved colors when Follow System accent color is chosen
* Added toggle to switch between calculation types
* History view will no longer lose scroll position if you scroll and leave the view and come back to it
* When changing automatic deletion settings, it will now show a dialog if you have more hours stored than what you changed it to
* Added a slight ripple animation when clicking items
* Added a click animation when clicking the item menu in the history view
* Added fade out animmation when deleting all hours in the history view
* Added animation when changing sort method in history view
* Performance improvements when managing hours
* Added animation when removing or adding item in history
* Added fade in animation when scrolling for each item in history
* Performance improvements when editing hours