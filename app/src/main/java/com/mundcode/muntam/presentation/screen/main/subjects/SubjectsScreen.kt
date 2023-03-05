package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.toolbars.MTLogoToolbar
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTScreenBackground
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.muntam.navigation.Exams
import com.mundcode.muntam.navigation.SubjectAdd
import com.mundcode.muntam.navigation.SubjectModify
import com.mundcode.muntam.presentation.item.SubjectAddItem
import com.mundcode.muntam.presentation.model.BottomSheetModel
import com.mundcode.muntam.presentation.screen.subject_add.SubjectItem
import com.mundcode.muntam.util.hiltViewModel

@Composable
fun SubjectsScreen(
    onNavOutEvent: (route: String) -> Unit,
    onBottomSheetEvent: (BottomSheetModel) -> Unit,
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
            item(
                span = { GridItemSpan(2) }
            ) {
                Text(
                    text = "과목 리스트",
                    style = MTTextStyle.textBold20,
                    color = Gray900
                )
            }
            items(subjects) { item ->
                SubjectItem(
                    subject = item,
                    onClick = { onNavOutEvent(Exams.getRouteWithArgs(item.id)) },
                    onClickMore = {
                        onBottomSheetEvent(
                            BottomSheetModel.SubjectMoreBottomSheet(
                                onClickModify = {
                                    onNavOutEvent(SubjectModify.getRouteWithArgs(item.id))
                                },
                                onClickDelete = {
                                    viewModel.deleteSubject(item)
                                }
                            )
                        )
                    }
                )
            }

            item {
                SubjectAddItem(
                    onClick = {
                        onNavOutEvent(SubjectAdd.route)
                    }
                )
            }
        }

        // todo 애드몹 네이티브 추가
    }
}
