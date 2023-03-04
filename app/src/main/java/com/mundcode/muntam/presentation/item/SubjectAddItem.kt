package com.mundcode.muntam.presentation.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius12
import com.mundcode.designsystem.theme.MTOrange
import com.mundcode.muntam.R
import com.mundcode.muntam.presentation.screen.subject_add.SUBJECT_ITEM_HEIGHT_DP
import com.mundcode.muntam.presentation.screen.subject_add.SUBJECT_ITEM_WIDTH_DP

//@Composable
//fun SubjectAddItem(
//    onClick: () -> Unit = {},
//    modifier: Modifier = Modifier
//) {
//    val stroke = Stroke(
//        width = 1f,
//        pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, 1f), 0f)
//    )
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        Canvas(
//            modifier = Modifier
//                .size(
//                    width = SUBJECT_ITEM_WIDTH_DP.dp,
//                    height = SUBJECT_ITEM_HEIGHT_DP.dp
//                )
//                .fillMaxSize()
//                .clickable(onClick = onClick)
//        ) {
//            drawRoundRect(
//                color = MTOrange,
//                style = stroke,
////                cornerRadius = CornerRadius(12f, 12f)
//            )
//        }
//
//        Column(
//            verticalArrangement = Arrangement.spacedBy(4.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_add_circle_32_dp),
//                contentDescription = null,
//                tint = MTOrange
//            )
//
//            Text(text = "과목 추가하기", style = MTTextStyle.text14, color = MTOrange)
//        }
//    }
//}

@Composable
fun SubjectAddItem(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_subject_add_btn),
        contentDescription = null,
        modifier = modifier
            .size(width = SUBJECT_ITEM_WIDTH_DP.dp, height = SUBJECT_ITEM_HEIGHT_DP.dp)
            .clip(CornerRadius12)
            .clickable(onClick = onClick),
        tint = MTOrange
    )
}

@Preview
@Composable
fun SubjectAddItemPreview() {
    SubjectAddItem()
}
