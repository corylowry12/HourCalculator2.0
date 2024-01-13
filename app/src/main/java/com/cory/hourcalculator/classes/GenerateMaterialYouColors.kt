package com.cory.hourcalculator.classes

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.cory.hourcalculator.sharedprefs.MaterialYouOptionData
import com.cory.hourcalculator.sharedprefs.RandomMaterialYouColorData

class GenerateMaterialYouColors(context: Context) {

    val insideContext = context

    fun generate_50() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_50
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_50
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_50
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_50
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_50
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_50
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_50
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_50
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_50
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_50
            }
        }

        return android.R.color.system_accent2_50
    }

    fun generate_100() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_100
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_100
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_100
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_100
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_100
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_100
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_100
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_100
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_100
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_100
            }
        }

        return android.R.color.system_accent2_100
    }

    fun generate_200() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_200
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_200
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_200
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_200
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_200
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_200
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_200
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_200
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_200
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_200
            }
        }

        return android.R.color.system_accent2_200
    }

    fun generate_300() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_300
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_300
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_300
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_300
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_300
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_300
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_300
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_300
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_300
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_300
            }
        }

        return android.R.color.system_accent2_300
    }

    fun generate_400() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_400
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_400
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_400
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_400
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_400
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_400
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_400
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_400
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_400
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_400
            }
        }

        return android.R.color.system_accent2_400
    }

    fun generate_500() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_500
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_500
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_500
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_500
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_500
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_500
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_500
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_500
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_500
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_500
            }
        }

        return android.R.color.system_accent2_500
    }

    fun generate_600() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_600
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_600
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_600
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_600
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_600
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_600
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_600
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_600
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_600
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_600
            }
        }

        return android.R.color.system_accent2_600
    }

    fun generate_800() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_800
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_800
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_800
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_800
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_800
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_800
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_800
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_800
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_800
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_800
            }
        }

        return android.R.color.system_accent2_800
    }

    fun generate_900() : Int {
        if (MaterialYouOptionData(insideContext).loadMaterialOption() == 1) {
            return android.R.color.system_accent1_900
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 2) {
            return android.R.color.system_accent2_900
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 3) {
            return android.R.color.system_accent3_900
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 4) {
            return android.R.color.system_neutral1_900
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 5) {
            return android.R.color.system_neutral2_900
        }
        else if (MaterialYouOptionData(insideContext).loadMaterialOption() == 6) {
            if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
                return android.R.color.system_accent1_900
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
                return android.R.color.system_accent2_900
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
                return android.R.color.system_accent3_900
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
                return android.R.color.system_neutral1_900
            }
            else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
                return android.R.color.system_neutral2_900
            }
        }

        return android.R.color.system_accent2_900
    }



    fun generate_random_500() : String {
        if (RandomMaterialYouColorData(insideContext).loadRandomState() == 1) {
            return "#${ Integer.toHexString(
                ContextCompat.getColor(
                    insideContext, android.R.color.system_accent1_500
                )
            )
            }"
        }
        else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 2) {
            return "#${ Integer.toHexString(
                ContextCompat.getColor(
                    insideContext, android.R.color.system_accent2_500
                )
            )
            }"
        }
        else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 3) {
            return "#${ Integer.toHexString(
                ContextCompat.getColor(
                    insideContext, android.R.color.system_accent3_500
                )
            )
            }"
        }
        else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 4) {
            return "#${ Integer.toHexString(
                ContextCompat.getColor(
                    insideContext, android.R.color.system_neutral1_500
                )
            )
            }"
        }
        else if (RandomMaterialYouColorData(insideContext).loadRandomState() == 5) {
            return "#${ Integer.toHexString(
                ContextCompat.getColor(
                    insideContext, android.R.color.system_neutral2_500
                )
            )
            }"
        }

        return "#${ Integer.toHexString(
            ContextCompat.getColor(
                insideContext, android.R.color.system_accent1_500
            )
        )
        }"
    }
}