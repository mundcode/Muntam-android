package com.mundcode.designsystem.components.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.CornerRadius8
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.MTToastBackground
import com.mundcode.designsystem.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastState(
    private val duration: Long = 2000
) {
    var show: Boolean = false

    suspend fun showToast() {
        CoroutineScope(Dispatchers.Default).launch {
            show = true
            delay(duration)
            show = false
        }
    }
}



@Composable
fun MTToast(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MTTextStyle.text14,
        color = White,
        textAlign = TextAlign.Center,
        modifier = modifier
            .background(color = MTToastBackground, shape = CornerRadius8)
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}

@Preview
@Composable
fun MTToastPreview() {
    MTToast(text = "새로운 과목이 추가되었습니다.", modifier = Modifier.padding(20.dp))
}