package com.mundcode.muntam

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val route: String
}

interface MutamDestination : Destination

interface MuntamBottomDestination : Destination {
    val icon: ImageVector
}

object Main : MutamDestination {
    override val route = "main"
}

object Subjects : MuntamBottomDestination {
    override val icon = Icons.Filled.List
    override val route = "subjects"
}

object FavoriteQuestions : MuntamBottomDestination {
    override val icon = Icons.Filled.Star
    override val route = "favorite_questions"
}

object Settings : MuntamBottomDestination {
    override val icon = Icons.Filled.Settings
    override val route = "settings"
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


