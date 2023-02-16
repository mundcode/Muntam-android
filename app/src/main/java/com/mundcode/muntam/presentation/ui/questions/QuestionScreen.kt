package com.mundcode.muntam.presentation.ui.questions

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.mundcode.muntam.util.sharedActivityViewModel

@Composable
fun QuestionScreen(
    viewModel: QuestionViewModel = sharedActivityViewModel()
) {
    val list = viewModel.questions.collectAsState(listOf())
    LazyColumn() {
    }
}
