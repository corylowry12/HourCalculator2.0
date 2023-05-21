package com.cory.hourcalculator.classes

class PatchNotesChanges {

    var bugFixesArray = arrayOf(
        "Fixed issue where when resetting \'App Settings\' to default it would uncheck the Vibration switch even though vibration is enabled after resetting",
        "Fixed issue with there being no vibration when long clicking items in history view",
        "Fixed issue with there being no vibration when clicking \"Hide\" to hide checkboxes",
        "Fixed issue with there being no vibration when clicking \"Report A Bug\" in the about app view",
        "Fixed issue with the shape of the floating action button in the history view",
        "Fixed crashing when trying to leave the edit hour view if there were pending changes and you clicked yes to save them",
        "Fixed issue with title and menu buttons not being centered properly in title bars",
        "Fixed issue with github button in about view still using built in web view instead of custom tab",
        "Fixed issue when hitting back button and the bottom nav bar wouldn't change selected tab",
        "Fixed issue where if you went to edit an entry and the in time or out time hours were equal to 12, it would change it to 11",
        "Fixed issue with deleting selected not working properly if you deleted multiple entries in quick succession one at a time",
        "Fixed some issues with weird splash screen logos on certain devices",
        "Fixed issue with there being not vibration when clicking the view repo button in the about app view",
        "Fixed issue where the date would be December 31st, 1969 when undoing hour deletion in the history settings view",
        "And so much more"
    )

    var newFeaturesArray = arrayOf(
        "Completely new and reworked theming system that allows you to pick basically whatever color you want",
        "Added the ability to click on the history tab when the history view is active and scroll to the top",
        "Added the ability to calculate in time or decimal format depending on which is enabled by long pressing the calculate button (eg. if decimal format is enabled, long pressing will calculate in time format)",
        "Added the ability to click on an hour entry and open it to edit it (disabled by default, to enable go to Settings->App Settings->Open hour for editing on history item click)",
        "Added a toggle to change menu item tint to match the theme",
        "Added a toggle to make badges in the navigation bar match the theme",
        "Added a toggle where if you reach the limit you can have it automatically delete all entries instead of just the oldest one",
        "And so much more"
    )

    var enhancementsArray = arrayOf(
        "Enabling and disabling history will now use a switch instead of radio buttons",
        "The switches in the settings will now have an icon based on their value",
        "Converted dialogs throughout the app to be Bottom Sheets",
        "Made the entire view clickable when selecting multiple hours to delete, it will function the same way as the checkbox (clicking will select and long clicking will select all)",
        "App will now show \"Error\" if there was a problem calculating wages",
        "App will now calculate hours via the info dialog in the history view to show HOURS:MINUTES if there is no decimal time stored",
        "Optimizations when launching the app",
        "Optimizations in switching tabs",
        "Improved the quality of the app icon logos in the Appearance Settings view",
        "Status bar will now match the color of the theme and will change color based on scroll position",
        "App will no longer let you go to another tab when edit view is visible to prevent accidentally leaving and losing edited data",
        "Adjusted left and right margins for the output text view on the home view",
        "App will now only use custom tabs instead of the previous built in web view",
        "Custom tabs will now match the accent color of the app",
        "Changed red 1 logo when there was a new update in patch notes setting item",
        "Updated themed icon to match the other regular icons",
        "Changed the chip color in the patch notes and time cards view to match the theming better",
        "Delete menu item in the edit view is now an icon instead of a drop down menu",
        "Removed auto icon theming, you will now just have to manually pick an app icon, this way the app doesn't have to restart every time you change a theme",
        "Major refactor of code to optimize the history view",
        "Added a toast message when entry is automatically deleted",
        "And so much more"
    )

    var bugFixesArrayInternal = arrayOf(
        "Fixed issue with app not changing theme in time cards view when device theme changed",
        "Fixed issue with app not changing theme in time card info view when device theme changed",
        "Fixed issue with dialogs not changing theme when device changed theme in time card info view",
        "Fixed issue with app not changing theme in saved colors view when device theme changed",
        "Fixed issue with app not changing theme in the app icons view when device theme changed",
        "Fixed issue with app not changing theme in the vibration settings view when device theme changed",
        "Fixed issue with dialogs not changing theme in the app settings view when device theme changed",
        "Fixed issue with history settings not changing theme properly when device theme changed",
        "Fixed issue with dialogs not changing theme in history settings when device theme changed",
        "Fixed issue with number of days view not changing theme properly when device theme changed",
        "Fixed issue with time card settings not changing theme properly when device theme changed",
        "Fixed issue with wage settings not changing theme properly when device theme changed",
        "Fixed issue with animation settings not changing theme when device theme changed",
        "Fixed issues with Accent Color settings not resetting properly when resetting using the reset button",
        "Fixed issues with vibration occurring twice when resetting settings",
        "Fixed issues with reset button in history settings view not resetting properly",
        "Fixed issues when resetting app settings",
        "Fixed issue with double vibration when clicking the export button",
        "Fixed issue with the select/take image dialog not disappearing in the time card info view under certain conditions",
        "Fixed issue with app crashing when you would delete all app data and had an icon other than teal selected"
    )

    var newFeaturesArrayInternal = arrayOf(
        "Added option to show break time in decimal instead of minutes",
        "Added reset button to break time settings",
        "Added reset button to vibration settings",
        "Added reset button to time card settings",
        "Added reset button to wage settings",
        "Added reset button to animation settings"
    )

    var enhancementsArrayInternal = arrayOf(
        "Migrated break time options to their own page in App Settings->Break Time Settings",
        "Updated dependencies in the Version Info page"
    )


}