package com.mundcode.designsystem.components.toolbars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.R

@Composable
fun MTLogoToolbar(
    modifier: Modifier = Modifier,
    icons: List<@Composable () -> Unit> = listOf()
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .height(44.dp)
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .padding(start = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = null
        )

        icons.forEach {
            it()
        }
    }
}
