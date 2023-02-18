package com.mundcode.muntam.presentation.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mundcode.muntam.R

val MTFontFamily = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold)
)

object MTTextStyle {
    val textBold20 = TextStyle(
        fontSize = 20.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp,
        letterSpacing = (-0.03).sp
    )

    val textBold16 = TextStyle(
        fontSize = 16.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
        letterSpacing = (-0.03).sp
    )

    val textBold14 = TextStyle(
        fontSize = 14.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp,
        letterSpacing = (-0.03).sp
    )

    val textBold13 = TextStyle(
        fontSize = 13.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 18.sp,
        letterSpacing = (-0.03).sp
    )

    val text20 = TextStyle(
        fontSize = 20.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        letterSpacing = (-0.03).sp
    )

    val text16 = TextStyle(
        fontSize = 16.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = (-0.03).sp
    )

    val text14 = TextStyle(
        fontSize = 14.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp,
        letterSpacing = (-0.03).sp
    )

    val text13 = TextStyle(
        fontSize = 13.sp,
        fontFamily = MTFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp,
        letterSpacing = (-0.03).sp
    )
}