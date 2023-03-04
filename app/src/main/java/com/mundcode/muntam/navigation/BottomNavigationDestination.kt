package com.mundcode.muntam.navigation

import com.mundcode.muntam.R

interface MuntamBottomDestination : Destination {
    val selectedIcon: Int
    val unselectedIcon: Int
    val display: String
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
