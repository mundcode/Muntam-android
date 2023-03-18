package com.mundcode.muntam.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface MuntamDestination : Destination

object Main : MuntamDestination {
    override val route = "main"

    val screens = listOf(
        Subjects,
        FavoriteQuestions,
        Settings
    )
}

object SubjectAdd : MuntamDestination {
    override val route = "subject_addition"
}

object SubjectModify : MuntamDestination {
    override val route = "subject_modify"
    val subjectIdArg = "subject_id"
    val routeWithArgs = "$route/{$subjectIdArg}"
    val arguments = listOf(
        navArgument(subjectIdArg) { type = NavType.IntType }
    )

    fun getRouteWithArgs(subjectIdArg: Int) = "$route/$subjectIdArg"
}

object SubjectSettings : MuntamDestination {
    override val route = "subject_settings"
}

object Exams : MuntamDestination {
    override val route = "exams"
    const val subjectIdArg = "subject_id"
    val routeWithArgs = "$route/{$subjectIdArg}"
    val arguments = listOf(
        navArgument(subjectIdArg) { type = NavType.IntType }
    )

    fun getRouteWithArgs(subjectIdArg: Int) = "$route/$subjectIdArg"
}

object ExamRecord : MuntamDestination {
    override val route = "exam_record"
    const val subjectIdArg = "subject_id"
    const val examIdArg = "exam_id"
    val routeWithArgs = "$route/{$subjectIdArg}/{$examIdArg}"
    val arguments = listOf(
        navArgument(subjectIdArg) { type = NavType.IntType },
        navArgument(examIdArg) { type = NavType.IntType }
    )

    fun getRouteWithArgs(subjectId: Int, examId: Int) = "$route/$subjectId/$examId"
}

object Questions : MuntamDestination {
    override val route = "questions"
    const val examIdArg = "exam_id"
    val routeWithArgs = "$route/{$examIdArg}"
    val arguments = listOf(
        navArgument(examIdArg) { type = NavType.IntType }
    )

    fun getRouteWithArgs(examId: Int) = "$route/$examId"
}

object QuestionSettings : MuntamDestination {
    override val route = "question_editor"
}

object QuestionDetails : MuntamDestination {
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
