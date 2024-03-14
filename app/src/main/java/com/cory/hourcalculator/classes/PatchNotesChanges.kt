package com.cory.hourcalculator.classes

class PatchNotesChanges {

    var bugFixesArray = arrayOf(
        "Fixed issue where cards would be the wrong color if material you was enabled and the background color was set to follow system and it was light theme",
        "Fixed issue where switches and radio buttons would animate when loading the view",
        "Fixed issue where the app would vibrate twice when changing a date when editing an hour",
        "Fixed issue with cards style in the About view being different than the rest of the app",
        "Fixed issue where the history options icon would still show when changing sort method",
        "Fixed issue with crashing when showing checkbox on certain versions of Android if \"Colored Menu Items\" was enabled",
        "Fixed issue with wage settings in the Settings view having the wrong subtitle",
        "Fixed issue where if you saved a color and changed the color and then manually selected the saved color again, the custom color subtitle would not show #color (Saved)",
        "Fixed issue where if the name text box in the time card settings view had focus and you clicked the back button it would go back instead of clearing focus from the text box"
    )

    var newFeaturesArray = arrayOf(
        "Added the option to do a backup/restore of the app",
        "Added the option to disable settings view change animations",
        "Added the option to disable recycler view animations",
        "Added the ability to show info for only selected items (disabled by default, can change at Settings->History Settings->Show info for only selected items)",
        "Added the option to disable Time Cards (Settings->Time Card Settings->Toggle Time Cards)",
        "Added the ability to adjust the overtime ratio (default was 1.5x) in Settings->Wage Settings"
    )

    var enhancementsArray = arrayOf(
        "Improved the responsiveness when changing tabs",
        "Removed the option to disable image animations",
        "Improved functionality of the \"Delete app data\" option in Settings",
        "Overtime will now be calculated in Time Cards",
        "Renamed the \"Calculate Overtime In History\" setting to \"Calculate Overtime\"",
        "The date button when editing an hour will no longer say \"Click on me to change date\"",
        "Black theme icon and the follow system icon in the Background Color view will now change color slightly if Make background more colorful is enabled",
        "Updated the minimum supported Android version",
        "Updated the design of the color picker sliders",
        "Various performance improvements in the History view",
        "Tweaked the margins of the sliders when selecting a custom color",
        "Adjusted the naming of various settings",
        "Updated the hint for the wages text box in the Wage settings view",
        "Added 8 different presets to choose from when selecting a color under Settings->Accent color->Custom color"
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