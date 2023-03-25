package com.mundcode.muntam.presentation.screen.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.WorkManager
import com.mundcode.designsystem.components.toast.MTToast
import com.mundcode.designsystem.components.toolbars.MTTitleToolbar
import com.mundcode.designsystem.theme.DefaultHorizontalPadding
import com.mundcode.designsystem.theme.Gray100
import com.mundcode.designsystem.theme.Gray600
import com.mundcode.designsystem.theme.Gray900
import com.mundcode.designsystem.theme.MTTextStyle
import com.mundcode.designsystem.theme.SortTabHeight
import com.mundcode.designsystem.theme.White
import com.mundcode.domain.model.enums.QuestionSort
import com.mundcode.muntam.presentation.item.AdmobBanner
import com.mundcode.muntam.presentation.item.QuestionItem
import com.mundcode.muntam.util.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun QuestionScreen(
    viewModel: QuestionsViewModel = hiltViewModel(),
    onBackEvent: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    val sortState by viewModel.currentSort.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        launch {
            viewModel.alarmRequestEvent.collectLatest {
                WorkManager.getInstance(context).enqueue(it)
            }
        }

        launch {
            viewModel.alarmCancelEvent.collectLatest {
                WorkManager.getInstance(context).cancelAllWorkByTag(it)
            }
        }

        launch {
            viewModel.toast.collectLatest {
                viewModel.toastState.showToast(it)
            }
        }
    }

    Scaffold(topBar = {
        MTTitleToolbar(
            title = state.exam.name,
            onClickBack = onBackEvent
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SortTabHeight),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortText(
                    text = "문제 번호 순",
                    select = sortState == QuestionSort.DEFAULT,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 20.dp, end = 8.dp)
                        .clickable(
                            enabled = sortState != QuestionSort.DEFAULT,
                            onClick = viewModel::onClickSortNumberAsc,
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        )
                )

                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .width(1.dp),
                    color = Gray100
                )

                SortText(
                    text = "오래 걸린 문제순",
                    select = sortState == QuestionSort.LAPS_DESC,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                        .clickable(
                            enabled = sortState != QuestionSort.LAPS_DESC,
                            onClick = viewModel::onClickSortLapsDesc,
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        )
                )

                Divider(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .width(1.dp),
                    color = Gray100
                )

                SortText(
                    text = "틀린 문제 먼저",
                    select = sortState == QuestionSort.WRONG_FIRST,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                        .clickable(
                            enabled = sortState != QuestionSort.WRONG_FIRST,
                            onClick = viewModel::onClickSortWrongFirst,
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        )
                )
            }

            Divider(modifier = Modifier.fillMaxWidth(), color = Gray100)

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomCenter) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.questions) { item ->
                        QuestionItem(
                            questionModel = item,
                            onClickAlarm = {
                                viewModel.onClickAlarm(item)
                            },
                            onClickCorrect = {
                                viewModel.onClickCorrect(item)
                            }
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = DefaultHorizontalPadding),
                            color = Gray100
                        )
                    }
                }

                MTToast(
                    toastState = viewModel.toastState,
                    modifier = Modifier
                        .padding(horizontal = DefaultHorizontalPadding)
                )
            }

            AdmobBanner(
                modifier = Modifier
                    .background(White)
                    .padding(horizontal = DefaultHorizontalPadding, vertical = 16.dp)
            )
        }
    }
}

@Composable
fun SortText(modifier: Modifier = Modifier, text: String, select: Boolean) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = if (select) MTTextStyle.textBold16 else MTTextStyle.text16,
            color = if (select) Gray900 else Gray600,
        )
    }
}
