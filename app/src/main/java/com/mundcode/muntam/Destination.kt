package com.mundcode.muntam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val route: String
}

interface MutamDestination : Destination

interface MuntamBottomDestination : Destination {
    val icon: ImageVector
    val display: String
}

object Main : MutamDestination {
    override val route = "main"

    val screens = listOf(
        Subjects,
        FavoriteQuestions,
        Settings
    )
}

object Subjects : MuntamBottomDestination {
    override val icon = Icons.Filled.List
    override val route = "subjects"
    override val display = "과목"
}

object FavoriteQuestions : MuntamBottomDestination {
    override val icon = Icons.Filled.Star
    override val route = "favorite_questions"
    override val display = "즐겨찾기"
}

object Settings : MuntamBottomDestination {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
    override val display = "설정"
}

// 과목 : Subject
object SubjectAddition : MutamDestination {
    override val route = "subject_addition"
}

object SubjectSettings : MutamDestination {
    override val route = "subject_settings"
}

// 시험 : Exam
object Exams : MutamDestination {
    override val route = "exams"
}

object ExamRecord : MutamDestination {
    override val route = "exam_record"
}

object ExamSettings : MutamDestination {
    override val route = "exam_settings"
}


// 문제 : Question
object Questions : MutamDestination {
    override val route = "questions"
}

object QuestionEditor : MutamDestination {
    override val route = "question_editor"
}

object QuestionDetails : MutamDestination {
    override val route = "question_details"
}

@Composable
fun Dummy(
    screenName: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = screenName, style = MaterialTheme.typography.h1, color = Color.Black)
    }
}


