package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.theme.MTScreenBackground
import com.mundcode.muntam.Exams
import com.mundcode.muntam.presentation.item.SubjectAddItem
import com.mundcode.muntam.presentation.screen.component.MTLogoToolbar
import com.mundcode.muntam.presentation.screen.subject_add.SubjectItem
import com.mundcode.muntam.util.hiltViewModel


@Composable
fun SubjectsScreen(
    onNavOutEvent: (route: String) -> Unit,
    viewModel: SubjectViewModel = hiltViewModel()
) {
    val subjects by viewModel.subjects.collectAsState(initial = listOf())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MTScreenBackground)
    ) {
        MTLogoToolbar()

        LazyVerticalGrid(
            columns = GridCells.Adaptive(162.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(subjects) { item ->
                SubjectItem(
                    subject = item,
                    onClick = { onNavOutEvent(Exams.getRouteWithArgs(item.id)) },
                    onClickMore = {
                        // todo 바텀시트 연결
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            item {
                SubjectAddItem(
                    onClick = {
                        // todo 과목 추가하기 화면으로 이동
                    }
                )
            }
        }

        // todo 애드몹 네이티브 추가
    }
}