package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.Food
import fi.nutrifier.ui.components.misc.Tag
import fi.nutrifier.ui.components.misc.TextTag
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.UserViewModel
import kotlin.math.roundToInt

@Composable
fun FoodButton(userViewModel: UserViewModel, food: Food, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextButton(
            onClick = { onClick() },
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(
                    modifier = Modifier.weight(0.8f)
                ) {
                    Text(text = food.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.padding(vertical = 4.dp))
                    if (food.fineliId != null) TextTag("Fineli")
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${ConversionUtils.convertEnergy(
                            food.calories,
                            userViewModel.user?.settings?.energyUnit
                        ).roundToInt()} ${userViewModel.user?.settings?.energyUnit?.displayName ?: "kcal"}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.padding(vertical = 2.dp))
                    Text(
                        text = FormattingUtils.generateMacroString(
                            fats = food.fat,
                            carbs = food.carbs,
                            protein = food.protein,
                            userViewModel = userViewModel,
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}
