package fi.nutrifier.models.database

data class UserMealPlan(
    val id: String?,
    val name: String?,
    val periods: List<GoalPeriod>,
    val createdAt: String?,
) {
    fun copyDeep(): UserMealPlan {
        return copy(
            periods = periods.map { it.copy() }
        )
    }
}