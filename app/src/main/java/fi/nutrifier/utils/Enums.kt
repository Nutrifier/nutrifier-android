package fi.nutrifier.utils

object Enums {
    enum class MealType {
        BREAKFAST, LUNCH, DINNER, SNACKS
    }

    enum class Screen {
        FOOD_ADD,
        FOOD_EDIT,
        COOKBOOK,
        DASHBOARD,
        LOGIN,
        FOOD_ENTRIES,
        MEAL,
        RECIPE_EDIT,
        RECIPE_VIEW,
        SHOPPING_LIST,
        BARCODE,
        SETTINGS,
        SETUP,
    }

    enum class Role(val displayName: String) {
        ADMIN("Admin"),
        PREMIUM("Premium"),
        REGULAR("Regular"),
    }

    enum class Diet(val displayName: String) {
        STANDARD("Standard"),
        VEGETARIAN("Vegetarian"),
        VEGAN("Vegan"),
        KETO("Keto"),
        LOW_CALORIE("Low calorie"),
        HIGH_PROTEIN("High protein"),
        PALEO("Paleo"),
        INTERMITTENT_FASTING("Intermittent fasting"),
        GLUTEN_FREE("Gluten free"),
    }

    enum class EnergyUnit(val displayName: String) {
        KCAL("kcal"),
        KJ("kJ"),
    }

    enum class FoodWeightUnit(val displayName: String) {
        GRAMS("g"),
        POUNDS("lb"),
        OUNCES("oz"),
    }

    enum class MacroWeightUnit(val displayName: String) {
        GRAMS("g"),
        OUNCES("oz"),
    }

    enum class IngredientUnit(val displayName: String) {
        ML("ml"),
        L("l"),
        TSP("tsp"),
        TBSP("tbsp"),
        MG("mg"),
        G("g"),
        KG("kg"),
        PINCH("pinch"),
        LB("lb"),
        OZ("oz"),
    }

    enum class Language(val displayName: String) {
        EN("English"),
        FI("Finnish"),
    }

    enum class GoalType(val displayName: String) {
        MAINTAIN("Maintain"),
        CUT("Cut"),
        BULK("Bulk"),
    }

    enum class Weekday(val index: Int, val displayName: String) {
        MONDAY(1, "Mon"),
        TUESDAY(2, "Tue"),
        WEDNESDAY(3, "Wed"),
        THURSDAY(4, "Thu"),
        FRIDAY(5, "Fri"),
        SATURDAY(6, "Sat"),
        SUNDAY(7, "Sun");

        companion object {
            fun fromIndex(index: Int): Weekday? {
                return entries.firstOrNull { it.index == index }
            }
        }
    }

    enum class NutrientDisplayMode(val displayName: String) {
        LEGACY_CIRCLE("Legacy circle"),
        FULL_CIRCLE("Full circle"),
        LINE("Line"),
    }

    enum class FoodMode(val displayName: String) {
        CREATE("Create"),
        EDIT_AMOUNT("Edit amount"),
        CREATE_ENTRY("Create entry"),
        VIEW("View")
    }

    enum class Sex(val displayName: String) {
        MALE("Male"),
        FEMALE("Female"),
        OTHER("Other"),
    }

    enum class ActivityLevel(val displayName: String, val description: String) {
        SEDENTARY(
            "Sedentary",
            "Little or no exercise. Mostly sitting during the day (desk job, minimal walking)."
        ),
        LIGHT(
            "Light",
            "Light exercise 1–3 days per week or daily walking. Mostly seated but some movement."
        ),
        MODERATE(
            "Moderate",
            "Moderate exercise 3–5 days per week (gym, cycling, jogging) or physically active job."
        ),
        ACTIVE(
            "Active",
            "Hard exercise 6–7 days per week or very physically demanding job (construction, warehouse)."
        ),
        VERY_ACTIVE(
            "Very active",
            "Intense training twice per day or endurance athlete with high daily physical load."
        )
    }

    enum class IndicatorSize {
        LARGE,
        SMALL
    }

    enum class Nutrition {
        CALORIES,
        FAT,
        CARBS,
        PROTEIN
    }

    enum class AnalyticsTimePeriod(val displayName: String) {
        WEEK("Week"),
        MONTH("Month"),
        YEAR("Year")
    }

    enum class DayGoalResult {
        MISSED, SUCCESS, FAILED, UNCERTAIN, FUTURE
    }

    enum class Status {
        SUCCESS, FAILED, TBD, UNCERTAIN
    }
}

