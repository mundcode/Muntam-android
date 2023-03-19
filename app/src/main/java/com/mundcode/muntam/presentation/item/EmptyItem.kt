package com.mundcode.muntam.presentation.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.Gray800
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.util.spToDp
import com.mundcode.muntam.R

@Composable
fun EmptyItem(
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "즐겨찾기한 시험이 없어요", style = MTTextStyle.text20.spToDp(), color = Gray800)

        Icon(
            painter = painterResource(id = R.drawable.ic_save_on_24_dp),
            contentDescription = null,
            tint = MTOrange
        )
    }

}