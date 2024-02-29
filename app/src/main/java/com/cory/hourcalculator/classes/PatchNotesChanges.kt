package com.cory.hourcalculator.classes

class PatchNotesChanges {

    var bugFixesArray = arrayOf(
        "Fixed issue where cards would be the wrong color if material you was enabled and the background color was set to follow system and it was light theme",
        "Fixed issue where switches and radio buttons would animate when loading the view",
        "Fixed issue where the app would vibrate twice when changing a date when editing an hour"
    )

    var newFeaturesArray = arrayOf(
        "Added the option to do a backup/restore of the app",
        "Added the option to disable settings view change animations",
        "Added the option to disable recycler view animations"
    )

    var enhancementsArray = arrayOf(
        "Improved the responsiveness when changing tabs",
        "Removed the option to disable image animations"
    )

    var bugFixesArrayInternal = arrayOf(
        "No new bug fixes"
    )

    var newFeaturesArrayInternal = arrayOf(
        "Added test for in app updater",
        "Added the ability to change between each Material You accent color",
        "Added the ability to reset all saved colors"
    )

    var enhancementsArrayInternal = arrayOf(
        "Added a scroll to top button in the Time Card Info view",
        "Tweaked the card outline color on selected items in the settings",
        "Added a confirmation dialog when deleting saved colors",
        "Delete app data dialog is now dismissible",
        "Redesigned the generate random button when selecting a color",
        "Redesigned the header text for all button sheets"
    )


}