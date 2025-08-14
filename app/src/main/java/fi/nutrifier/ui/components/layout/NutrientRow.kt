package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.Food

@Composable
fun NutrientRow(food: Food?) {
    Row {
        if (food != null) {
            Text("${food.calories}")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Box(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.outline)
                .align(Alignment.CenterVertically)
                .width(1.dp)
                .height(12.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text("${food.fat}")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Box(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.outline)
                .align(Alignment.CenterVertically)
                .width(1.dp)
                .height(12.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text("${food.carbs}")
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Box(modifier = Modifier
                .background(color = MaterialTheme.colorScheme.outline)
                .align(Alignment.CenterVertically)
                .width(1.dp)
                .height(12.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Text("${food.protein}")
        } else {
            Text("Loading...")
        }
    }
}