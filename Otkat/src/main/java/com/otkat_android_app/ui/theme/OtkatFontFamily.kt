package com.otkat_android_app.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.otkat_android_app.R

internal object OtkatFontFamily {
    val bigNoodleTooOblique = FontFamily(
        Font(R.font.bignoodletoooblique, FontWeight.Light),
        Font(R.font.bignoodletoooblique, FontWeight.Normal),
        Font(R.font.bignoodletoooblique, FontWeight.Medium),
        Font(R.font.bignoodletoooblique, FontWeight.Bold)
    )
}