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

interface MutamDestination : Destination

object Main : MutamDestination {
    override val route = "main"

    val screens = listOf(
        Subjects,
        FavoriteQuestions,
        Settings
    )
}

object SubjectAdd : MutamDestination {
    override val route = "subject_addition"
}

object SubjectSettings : MutamDestination {
    override val route = "subject_settings"
}

object Exams : MutamDestination {
    override val route = "exams"
    const val subjectIdArg = "subject_id"
    val routeWithArgs = "$route/{$subjectIdArg}"
    val arguments = listOf(
        navArgument(subjectIdArg) { type = NavType.IntType }
    )

    fun getRouteWithArgs(subjectIdArg: Int) = "$route/$subjectIdArg"
}

object ExamRecord : MutamDestination {
    override val route = "exam_record"
}

object Questions : MutamDestination {
    override val route = "questions"
}

object QuestionSettings : MutamDestination {
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
