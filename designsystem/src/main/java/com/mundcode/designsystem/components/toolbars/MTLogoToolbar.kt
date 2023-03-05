package com.mundcode.designsystem.components.toolbars

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.R

@Composable
fun MTLogoToolbar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = null
        )
    }
}
