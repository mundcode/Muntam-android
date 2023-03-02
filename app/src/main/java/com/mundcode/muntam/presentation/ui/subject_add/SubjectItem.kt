package com.mundcode.muntam.presentation.ui.subject_add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White
import com.mundcode.designsystem.util.spToDp
import com.mundcode.muntam.presentation.ui.model.SubjectModel

@Composable
fun SubjectItem(
    subject: SubjectModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(width = 162.dp, height = 156.dp)
            .padding(16.dp)
            .background(color = White, shape = CornerRadius12)
            .clickable(onClick = onClick),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // todo 컴포즈로 이모지 API 제대로 사용하기
            Text(
                text = subject.imoji,
                style = MTTextStyle.text20.spToDp(),
                modifier = Modifier.padding(2.dp)
            )

            Icon(
                painter = painterResource(id = com.mundcode.designsystem.R.drawable.ic_more_24_dp),
                contentDescription = null
            )
        }
    }
}