package com.mundcode.muntam

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
}

interface MutamDestination : Destination

interface MuntamBottomDestination : Destination {
    val selectedIcon: Int
    val unselectedIcon: Int
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
    override val unselectedIcon = R.drawable.ic_bottom_navigation_unselected_home
    override val selectedIcon: Int = R.drawable.ic_bottom_navigation_selected_home
    override val route = "subjects"
    override val display = "홈"
}

object FavoriteQuestions : MuntamBottomDestination {
    override val unselectedIcon = R.drawable.ic_bottom_navigation_unselected_favorite
    override val selectedIcon: Int = R.drawable.ic_bottom_navigation_selected_favorite
    override val route = "favorite_questions"
    override val display = "즐겨찾기"
}

object Settings : MuntamBottomDestination {
    override val unselectedIcon = R.drawable.ic_bottom_navigation_unselected_settings
    override val selectedIcon: Int = R.drawable.ic_bottom_navigation_selected_settings
    override val route = "settings"
    override val display = "설정"
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
