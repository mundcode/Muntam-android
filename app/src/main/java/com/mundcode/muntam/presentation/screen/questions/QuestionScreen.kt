package com.mundcode.muntam.presentation.screen.questions

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.mundcode.muntam.util.sharedActivityViewModel

@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = sharedActivityViewModel(),
    onBackEvent: () -> Unit,
    onNavEvent: () -> Unit
) {
    val list = viewModel.questions.collectAsState(listOf())
    LazyColumn() {
    }
}
