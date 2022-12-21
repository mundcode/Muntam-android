package com.mundcode.muntam.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace16
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace8

@Composable
fun MuntamToolbar(
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    title: String = "",
    icons: List<@Composable () -> Unit> = listOf()
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = DefaultSpace16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                MarginSpacer(dp = DefaultSpace8)
            }

            Text(
                text = title,
                style = MaterialTheme.typography.h6
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(DefaultSpace8)) {
            icons.forEach {
                it()
            }
        }
    }
}

