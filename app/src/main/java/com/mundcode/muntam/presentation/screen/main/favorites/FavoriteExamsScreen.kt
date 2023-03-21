package com.mundcode.muntam.presentation.screen.main.favorites

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.dialogs.alert.AlertDialog
import com.mundcode.designsystem.components.dialogs.textfeild.NameEditorDialog
import com.mundcode.designsystem.components.toast.MTToast
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.DefaultHorizontalPadding
import com.mundcode.designsystem.theme.Gray100
import com.mundcode.designsystem.theme.Gray200
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTLightOrange
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.White
import com.mundcode.muntam.presentation.item.AdmobBanner
import com.mundcode.muntam.presentation.item.ExamItem
import com.mundcode.muntam.presentation.item.FavoriteEmptyItem
import com.mundcode.muntam.presentation.model.BottomSheetModel
import com.mundcode.muntam.util.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FavoriteExamsScreen(
    viewModel: FavoriteExamViewModel = hiltViewModel(),
    onBottomSheetEvent: (BottomSheetModel) -> Unit,
    onNavOutEvent: (String) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        launch {
            viewModel.navigationEvent.collectLatest { route ->
                onNavOutEvent(route)
            }
        }

        launch {
            viewModel.toast.collectLatest { text ->
                viewModel.toastState.showToast(text)
            }
        }
    }

    Scaffold(topBar = {
        MTTitleToolbar(
            title = "즐겨찾는 시험",
            showBack = false,
            showBottomDivider = state.exams.isEmpty()
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            if (state.exams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    FavoriteEmptyItem(modifier = Modifier.align(Alignment.Center))
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        state.exams.onEachIndexed { entryIndex, (name, exams) ->
                            stickyHeader {
                                Column {
                                    Text(
                                        text = name,
                                        style = MTTextStyle.textBold16,
                                        color = Gray900,
                                        modifier = Modifier
                                            .background(MTLightOrange)
                                            .fillMaxWidth()
                                            .padding(top = 16.dp, bottom = 8.dp)
                                            .padding(horizontal = DefaultHorizontalPadding)
                                    )
                                    Divider(modifier = Modifier.fillMaxWidth(), color = Gray200)
                                }
                            }

                            itemsIndexed(exams) { index, item ->

                                Column {
                                    ExamItem(
                                        exam = item,
                                        onClick = {
                                            viewModel.onClickExam(item)
                                        },
                                        onClickMore = {
                                            viewModel.onClickOption(examModel = item)
                                            onBottomSheetEvent(
                                                BottomSheetModel.SubjectMoreBottomSheet(
                                                    onClickModify = {
                                                        viewModel.onSelectModifyFromBottomSheet()
                                                    },
                                                    onClickDelete = {
                                                        viewModel.onSelectDeleteFromBottomSheet()
                                                    }
                                                )
                                            )
                                        },
                                        onClickSave = {
                                            viewModel.onClickFavorite(item)
                                        }
                                    )

                                    if (
                                        entryIndex != state.exams.size - 1 ||
                                        index != exams.lastIndex
                                    ) {
                                        Divider(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    horizontal =
                                                    if (index == exams.lastIndex) {
                                                        0.dp
                                                    } else {
                                                        DefaultHorizontalPadding
                                                    }
                                                ),

                                            color = if (index == exams.lastIndex) {
                                                Gray200
                                            } else {
                                                Gray100
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    MTToast(
                        toastState = viewModel.toastState,
                        modifier = Modifier
                            .padding(horizontal = DefaultHorizontalPadding)
                    )
                }
            }

            AdmobBanner(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = DefaultHorizontalPadding, vertical = 16.dp)
            )
        }

        if (state.showModifyDialog) {
            NameEditorDialog(
                hint = "과목명을 입력해주세요",
                onClickCancel = viewModel::onCancelDialog,
                onClickConfirm = viewModel::onModifyExamName
            )
        }

        if (state.showDeleteConfirmDialog) {
            AlertDialog(
                title = "시험 삭제하기",
                subtitle = "선택한 시험 리스트를 삭제합니다.\n삭제한 항목은 다시 되돌릴 수 없습니다.",
                confirmText = "삭제하기",
                onClickConfirm = viewModel::onSelectDeleteExam,
                onClickCancel = viewModel::onCancelDialog
            )
        }
    }
}
