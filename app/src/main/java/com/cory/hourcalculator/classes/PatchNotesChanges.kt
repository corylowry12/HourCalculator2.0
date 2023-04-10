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
        "Fixed issue where if you had an image stored in time cards and deleted that image from the device, it would show a blank image in gallery",
        "Fixed issue with break text box not being positioned correctly on certain devices",
        "Fixed issue with the background color nto being set properly around the ad on certain devices",
        "Fixed issue with the delete app data item in the settings not deleting all app data",
        "Fixed issue with the text box in the time card settings view for default name being the wrong color",
        "Fixed issue with the cursor in the text box when renaming something being the wrong color"
    )

    var newFeaturesArrayInternal = arrayOf(
        "Added where patch notes will show on app launch after an update",
        "Added ability to save a custom color by long clicking on the select button when choosing a custom color",
        "Added wages when viewing a time card entry",
        "Added wages to be shown in the time card info view",
        "Added the ability " +
                "to update wages if there is an error doing the calculation by long pressing the wages text view on any of the entries"
    )

    var enhancementsArrayInternal = arrayOf(
        "Reworked the color generation when material you is enabled",
        "Made background darker when material you and colorful background are enabled",
        "Specific animations will now be disabled by default as you will have better performance with them disabled",
        "App will now tell you to go set wages if wages are shown and wages are not set",
        "Delete app data will now use a bottom sheet instead of a dialog",
        "Dialog asking to confirm pending changes when trying to leave the edit entry view will now be a bottom sheet instead of a dialog",
        "Tweaked width of the save button when editing an entry",
        "Tweaked text size of the save button when editing an entry",
        "Updated width of the date button when editing an entry"
    )


}