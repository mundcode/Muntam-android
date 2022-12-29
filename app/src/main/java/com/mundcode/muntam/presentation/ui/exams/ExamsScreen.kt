package com.mundcode.muntam.presentation.ui.exams

import android.Manifest
import android.util.Log
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mundcode.muntam.Dummy

@Composable
fun ExamsScreen(
    subjectId: Int
) {
    Dummy(screenName = "$subjectId")
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