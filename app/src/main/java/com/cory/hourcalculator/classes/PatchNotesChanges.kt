package com.cory.hourcalculator.classes

class PatchNotesChanges {

    var bugFixesArray = arrayOf(
        "Fixed issue where cards would be the wrong color if material you was enabled and the background color was set to follow system and it was light theme"
    )

    var newFeaturesArray = arrayOf(
        "Added the ability to change between each Material You accent color"
    )

    var enhancementsArray = arrayOf(
        "Added a scroll to top button in the Time Card Info view",
        "Tweaked the card outline color on selected items in the settings"
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