package com.mundcode.muntam.presentation.ui.main.subjects

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundcode.designsystem.components.toast.CustomToast
import com.mundcode.designsystem.components.toast.MTToast
import com.mundcode.designsystem.components.toast.rememberToastState
import com.mundcode.designsystem.theme.Circle
import com.mundcode.designsystem.theme.DefaultSpace12
import com.mundcode.designsystem.theme.DefaultSpace16
import com.mundcode.designsystem.theme.DefaultSpace32
import com.mundcode.designsystem.theme.DefaultSpace4
import com.mundcode.designsystem.theme.DefaultSpace8
import com.mundcode.muntam.Exams
import com.mundcode.muntam.presentation.ui.component.MarginSpacer
import com.mundcode.muntam.presentation.ui.component.MuntamToolbar
import com.mundcode.muntam.presentation.ui.model.SubjectState
import com.mundcode.muntam.util.sharedActivityViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SubjectsScreen(
    onNavOutEvent: (route: String) -> Unit,
    viewModel: SubjectViewModel = sharedActivityViewModel()
) {
    val subjects by viewModel.subjects.collectAsState(listOf())

    Scaffold(
        topBar = {
            MuntamToolbar(
                showBack = false,
                title = "과목 선택",
                icons = listOf {
                    Icon(
                        imageVector = Icons.Default.AddCircle,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(Circle)
                    )
                }
            )
        }
    ) { paddingValue ->
        LazyColumn(
            Modifier.padding(paddingValue),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                Row {
                    Button(onClick = {
                        viewModel.insertSubject()
                    }) {
                        Text(text = "insertSubject")
                    }

                    Button(onClick = {
                        viewModel.updateSubject(subjects.random())
                    }) {
                        Text(text = "updateSubject")
                    }

                    Button(onClick = {
                        viewModel.deleteSubject(subjects.random())
                    }) {
                        Text(text = "deleteSubject")
                    }
                }
            }

            items(subjects) { subject ->
                SubjectListItem(
                    Modifier.padding(horizontal = DefaultSpace8),
                    subjectState = subject,
                    onClickSubject = {
                        onNavOutEvent(Exams.getRouteWithArgs(it.id))
                    }
                )
            }
        }
    }
}

@Composable
fun SubjectsContent(
    modifier: Modifier = Modifier,
    subjectStates: List<SubjectState>,
    onClickSubject: (SubjectState) -> Unit
) {
    SubjectsList(
        modifier = modifier,
        list = subjectStates,
        onClickSubject = { subject ->
            onClickSubject(subject)
        }
    )
}

@Composable
fun SubjectsList(
    state: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    list: List<SubjectState>,
    onClickSubject: (SubjectState) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = DefaultSpace32),
        verticalArrangement = Arrangement.spacedBy(DefaultSpace8)
    ) {
        items(list) { subject ->
            SubjectListItem(
                modifier.padding(horizontal = DefaultSpace8),
                subjectState = subject,
                onClickSubject = {
                    onClickSubject(subject)
                }
            )
        }
    }
}

@Composable
fun SubjectListItem(
    modifier: Modifier = Modifier,
    subjectState: SubjectState,
    onClickSubject: (SubjectState) -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = subjectState.backgroundColor, shape = MaterialTheme.shapes.large)
            .clip(shape = MaterialTheme.shapes.large)
            .clickable {
                onClickSubject(subjectState)
            }
            .padding(horizontal = DefaultSpace16, vertical = DefaultSpace12)

    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subjectState.id.toString(),
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold

                )

                Icon(
                    imageVector =
                    if (subjectState.pinned) {
                        Icons.Filled.Star
                    } else {
                        Icons.Outlined.Star
                    },
                    contentDescription = null
                )
            }

            MarginSpacer(dp = DefaultSpace4)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최근시험",
                    style = MaterialTheme.typography.caption,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Light
                )

                MarginSpacer(dp = DefaultSpace4)

                Text(
                    text = subjectState.subjectTitle.ifEmpty { "없음" },
                    style = MaterialTheme.typography.body2,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(widthDp = 320, heightDp = 400, backgroundColor = 0x00ffffff)
@Composable
fun PreviewSubjectListItem() {
    SubjectListItem(
        subjectState = SubjectState(
            subjectTitle = "수학",
            lastExamDate = "2022.12.10",
            pinned = false
        )
    ) {
    }
}

@Preview
@Composable
fun PreviewSubjectsList() {
    SubjectsList(
        list = (1..30).map {
            SubjectState(
                id = it,
                subjectTitle = "수학$it",
                lastExamDate = "2022.12.$it",
                pinned = it % 2 == 0
            )
        },
        onClickSubject = {
        }
    )
}
