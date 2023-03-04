package com.mundcode.muntam.presentation.screen.exams

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mundcode.muntam.util.sharedActivityViewModel

@Composable
fun ExamsScreen(
    subjectId: Int,
    viewModel: ExamsViewModel = sharedActivityViewModel(),
    onNavEvent: (String) -> Unit
) {
    val exams = viewModel.exams.collectAsState(listOf())

    LaunchedEffect(key1 = true) {
        viewModel.getExams(subjectId)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Row {
                Button(onClick = {
                    viewModel.insertExam(subjectId)
                }) {
                    Text(text = "insert")
                }

                Button(onClick = {
                    viewModel.updateExam(exams.value.random())
                }) {
                    Text(text = "update")
                }

                Button(onClick = {
                    viewModel.deleteExam(exams.value.random())
                }) {
                    Text(text = "delete")
                }
            }
        }

        items(exams.value) { model ->
            Text(text = model.name, fontSize = 42.sp)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TestScreen() {
    val notificationPermissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS) { result ->
            Log.d("SR-N", "result $result")
        }

    val text by derivedStateOf {
        when {
            notificationPermissionState.status.isGranted -> "이미 권한 허용됨"
            notificationPermissionState.status.shouldShowRationale -> "Show rationale"
            else -> "권한 요청"
        }
    }

    Button(onClick = {
        if (!notificationPermissionState.status.isGranted) {
            if (notificationPermissionState.status.shouldShowRationale) {
                Log.d("SR-N", "Show rationale")
            } else {
                notificationPermissionState.launchPermissionRequest()
            }
        }
    }) {
        Text(text = text)
    }
}
