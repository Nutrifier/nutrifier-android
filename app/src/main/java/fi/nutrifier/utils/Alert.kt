package fi.nutrifier.utils

enum class AlertType {
    ERROR,
    WARNING,
    INFO,
    SUCCESS,
}

data class Alert(
    val message: String,
    val type: AlertType? = AlertType.ERROR,
)