package com.mundcode.muntam.presentation.ui.main.subjects

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mundcode.muntam.presentation.ui.component.MarginSpacer
import com.mundcode.muntam.presentation.ui.component.MuntamToolbar
import com.mundcode.muntam.presentation.ui.theme.Circle
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace12
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace16
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace32
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace4
import com.mundcode.muntam.presentation.ui.theme.DefaultSpace8
import com.mundcode.muntam.util.sharedActivityViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SubjectsScreen(
    onNavOutEvent: (route: String) -> Unit,
    viewModel: SubjectViewModel = sharedActivityViewModel()
) {
    val notificationPermissionState = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    ) { result ->
        Log.d("SR-N", "notificationPermissionState result $result")
    }

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
                            .clickable {
                                Log.d("SR-N", "clickable")

                                if (!notificationPermissionState.status.isGranted) {
                                    Log.d("SR-N", "isGranted")

                                    if (notificationPermissionState.status.shouldShowRationale) {
                                        Log.d("SR-N", "shouldShowRationale")
                                    } else {
                                        Log.d(
                                            "SR-N",
                                            "shouldShowRationale not -> launchPermissionRequest"
                                        )

                                        notificationPermissionState.launchPermissionRequest()
                                    }
                                }
                                // onNavOutEvent(SubjectAdd.route)
                            }
                    )
                }
            )
        }
    ) { paddingValue ->
        Column(Modifier.padding(paddingValue)) {
            Button(onClick = {
            }) {
                Text(text = "setS")
            }

            Button(onClick = {
                viewModel.getS()
            }) {
                Text(text = "getS")
            }

            Button(onClick = {
                viewModel.setE()
            }) {
                Text(text = "setE")
            }

            Button(onClick = {
                viewModel.getE()
            }) {
                Text(text = "getE")
            }

        }
//        SubjectsContent(
//            modifier = Modifier.padding(paddingValue),
//            onClickSubject = {
//                onNavOutEvent(Exams.getRouteWithArgs(it.id))
//            }
//        )
    }
}

@Composable
fun SubjectsContent(
    modifier: Modifier = Modifier,
    onClickSubject: (Subject) -> Unit
) {
    SubjectsList(
        modifier = modifier,
        list = (1..30).map {
            Subject(
                id = it,
                subjectTitle = "수학$it",
                lastExamDate = "2022.12.$it",
                pinned = it % 2 == 0
            )
        },
        onClickSubject = { subject ->
            onClickSubject(subject)
        }
    )
}

@Composable
fun SubjectsList(
    state: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
    list: List<Subject>,
    onClickSubject: (Subject) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = DefaultSpace32),
        verticalArrangement = Arrangement.spacedBy(DefaultSpace8)
    ) {
        items(list) { subject ->
            SubjectListItem(
                modifier.padding(horizontal = DefaultSpace8),
                subject = subject,
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
    subject: Subject,
    onClickSubject: () -> Unit
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(color = subject.backgroundColor, shape = MaterialTheme.shapes.large)
            .clip(shape = MaterialTheme.shapes.large)
            .clickable {
                onClickSubject()
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
                    text = subject.subjectTitle,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold

                )

                Icon(
                    imageVector =
                    if (subject.pinned) {
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
                    text = subject.lastExamDate.ifEmpty { "없음" },
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
        subject = Subject(
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
            Subject(
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

data class Subject(
    val id: Int = 0,
    val subjectTitle: String,
    val backgroundColor: Color = Color.DarkGray,
    val lastExamDate: String,
    val pinned: Boolean
)
