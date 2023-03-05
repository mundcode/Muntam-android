package com.mundcode.muntam.presentation.screen.exams

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.muntam.util.hiltViewModel

@Composable
fun ExamsScreen(
    viewModel: ExamsViewModel = hiltViewModel(),
    onNavEvent: (String) -> Unit,
    onClickBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    Scaffold(
        topBar = {
            MTTitleToolbar(
                title = state.subjectTitle,
                onClickBack = onClickBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.exams) { item ->
                    Column {
                        ExamItem(exam = item)
                        Divider(Modifier.fillMaxWidth(), color = Gray200)
                    }
                }
            }

            // todo 시간 기록하기 버튼 추가
            // todo 광고 뷰 추가
        }
    }

    // todo 다이얼로그들 추가
    // todo 토스트 추가

}
