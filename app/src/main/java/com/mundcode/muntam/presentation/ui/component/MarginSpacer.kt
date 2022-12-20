package com.mundcode.muntam.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun RowScope.MarginSpacer(dp: Dp) {
    Spacer(modifier = Modifier.width(dp))
}

@Composable
fun ColumnScope.MarginSpacer(dp: Dp) {
    Spacer(modifier = Modifier.height(dp))
}