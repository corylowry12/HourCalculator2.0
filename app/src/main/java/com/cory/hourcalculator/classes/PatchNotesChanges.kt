package com.cory.hourcalculator.classes

class PatchNotesChanges {

    var bugFixesArray = arrayOf(
        "Fixed issue with bottom navigation bar not being colored properly when generate a random color on app launch was enabled",
        "Fixed issue with the Time Card Scrolling Animation setting not saving",
        "Fixed issue with options bottom sheet in History not sliding up all the way on tablets",
        "Fixed issue with the bottom sheets in Time Cards not sliding up all the way on tablets",
        "Fixed issue with the permissions bottom sheet not sliding up all the way on tablets",
        "Fixed issue with crashing when the app would set a certain theme in the home view",
        "Fixed issue with app not resetting the chosen hex color in the Accent Color view when resetting",
        "Fixed issue with vibration causing crashing on versions of Android 11 and below",
        "Fixed issue with app crashing upon launch due to setting menu icon colors",
        "Fixed issue with app crashing when loading history, will now prompt to delete history",
        "Fixed issue where app would occasionally only show one minute decimal place when calculating hours in time format",
        "Fixed issue with app sending out a toast message saying \"called\" when clicking the History tab under certain conditions",
        "Fixed issue with bottom navigation bar not updating in some cases",
        "Fixed issue with crashing when opening app settings",
        "Fixed issue with crashing when opening history settings"
    )

    var newFeaturesArray = arrayOf(
        "Added support for tablets",
        "Added tablet settings for tablets",
        "Added options to reposition items in the navigation rail for tablets",
        "Added the option to set the History FAB to be positioned on the left, center, or right",
        "App will no longer show the take a photo button if the device does not have a camera",
        "Disabled colored menu tint settings on Android versions less than 10"
    )

    var enhancementsArray = arrayOf(
        "Added support for auto rotation for tablets",
        "Vibration settings will no longer be shown in App Settings if the device doesn't support vibration",
        "History will now be two columns on tablets and one column on smart phones",
        "Time Cards will now be two columns on tablets and one column on smart phones",
        "Added error checking that will now prompt you to delete history if there is an error loading items",
        "App will no longer show the take a photo button if the device does not have a camera"
    )

    var bugFixesArrayInternal = arrayOf(
        "Fixed issue with app sending out a toast message saying \"called\" when clicking the History tab under certain conditions",
        "Fixed issue with bottom navigation bar not updating in some cases",
        "Fixed issue with crashing when opening app settings",
        "Fixed issue with crashing when opening history settings"
    )

    var newFeaturesArrayInternal = arrayOf(
        "Added the option to set the History FAB to be positioned on the left, center, or right",
        "Added the option to set the Time Card FAB to be positioned on the left, center, or right"
    )

    var enhancementsArrayInternal = arrayOf(
        "App will no longer show the take a photo button if the device does not have a camera",
        "Disabled colored menu tint settings on Android versions less than 10"
    )


}