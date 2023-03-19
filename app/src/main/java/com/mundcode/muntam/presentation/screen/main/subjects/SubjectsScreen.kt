package com.mundcode.muntam.presentation.screen.main.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mundcode.designsystem.components.dialogs.alert.AlertDialog
import com.mundcode.designsystem.components.toolbars.MTLogoToolbar
import com.mundcode.designsystem.theme.Gray100
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.muntam.navigation.Exams
import com.mundcode.muntam.navigation.SubjectAdd
import com.mundcode.muntam.navigation.SubjectModify
import com.mundcode.muntam.presentation.item.AdmobBanner
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
    val state by viewModel.state.collectAsState()
    val subjects = state.subjects

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Gray100)
    ) {
        MTLogoToolbar()

        LazyVerticalGrid(
            columns = GridCells.Adaptive(162.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .weight(1f)
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
                                    viewModel.onClickDeleteSubject(item)
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

        AdmobBanner(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 16.dp, top = 8.dp)
        )
    }

    if (state.showDeleteConfirmDialog) {
        AlertDialog(
            title = "과목 삭제하기",
            subtitle = "선택한 과목 리스트를 삭제합니다.\n삭제한 항목은 다시 되돌릴 수 없습니다.",
            cancelText = "취소",
            confirmText = "삭제하기",
            onClickCancel = {
                viewModel.onCancelDialog()
            },
            onClickConfirm = {
                viewModel.onClickDeleteSubjectConfirm()
            }
        )
    }
}
