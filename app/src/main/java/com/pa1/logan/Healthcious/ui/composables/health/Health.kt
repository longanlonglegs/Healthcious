package com.pa1.logan.Healthcious.ui.composables.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.VM.HealthVM
import com.pa1.logan.Healthcious.ui.composables.misc.StarRatingBar

@Composable
fun Health (navController: NavController?){

    var healthVM = HealthVM()

    var sugar by remember { mutableStateOf(healthVM.totalSugar) }
    var salt by remember { mutableStateOf(healthVM.totalSalt) }
    var caloricIntake by remember { mutableStateOf(healthVM.totalCalories) }
    var healthStars by remember { mutableStateOf(5f) }

    Column (
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)) {

        ProgressCircle(caloricIntake, 300, 20)

        Card (
            modifier = Modifier.fillMaxWidth()
        ){
            Row(Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Sugar Intake",
                    textAlign = TextAlign.Start,
                    modifier = Modifier,
                    fontSize = 25.sp
                )
                Text(
                    sugar.toString(),
                    textAlign = TextAlign.End,
                    modifier = Modifier,
                    fontSize = 25.sp
                )
                ProgressCircle(sugar, 30, 5)
            }
        }

        Card (
            modifier = Modifier.fillMaxWidth()
        ){
            Row(Modifier
                .fillMaxWidth()
                .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Salt Intake", textAlign = TextAlign.Start, modifier = Modifier, fontSize = 25.sp)
                Text(salt.toString(), textAlign = TextAlign.End, modifier = Modifier, fontSize = 25.sp)
                ProgressCircle(salt, 30, 5)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth())
        {
            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                StarRatingBar(
                    maxStars = 5,
                    rating = healthStars,
                    onRatingChanged = { healthStars = it }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("graph")
        }

    }
}

@Composable
fun ProgressCircle(progress: Float, size: Int, width: Int) {
    CircularProgressIndicator(
        progress = {progress},
        modifier = Modifier.size(size.dp),
        strokeWidth = width.dp,
        trackColor = Color.Magenta,
    )
}

@Preview(showBackground = true)
@Composable
fun HealthPreview() {
    AppTheme {
        Health(navController = null)
    }
}
