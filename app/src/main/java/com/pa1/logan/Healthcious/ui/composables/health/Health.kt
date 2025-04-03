package com.pa1.logan.Healthcious.ui.composables.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.VM.HealthVM
import com.pa1.logan.Healthcious.ui.composables.misc.StarRatingBar

@Composable
fun Health (navController: NavController?, paddingValues: PaddingValues){

    var healthVM = HealthVM()

    var sugar by remember { mutableStateOf(healthVM.totalSugar) }
    var salt by remember { mutableStateOf(healthVM.totalSalt) }
    var caloricIntake by remember { mutableStateOf(healthVM.totalCalories) }
    var healthStars by remember { mutableStateOf(5f) }
    var currentCalories by remember{ mutableStateOf(0f)}

    Column (
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)) {

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                ProgressCircle(caloricIntake, 200, 20)
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
                    Text(
                        "Total Calorie",
                        textAlign = TextAlign.Start,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${currentCalories}/${ healthVM.totalCalories }",
                        textAlign = TextAlign.Start,
                        fontSize = 25.sp,
                    )
                }
            }
        }

        Card (
            modifier = Modifier.fillMaxWidth().padding(5.dp)
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
            modifier = Modifier.fillMaxWidth().padding(5.dp)
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
            modifier = Modifier.fillMaxWidth().padding(5.dp))
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
            modifier = Modifier.fillMaxSize().padding(5.dp)
        ) {
            Text("graph")
        }

    }
}

@Composable
fun ProgressCircle(progress: Float, size: Int, width: Int) {
    CircularProgressIndicator(
        progress = {progress},
        modifier = Modifier
            .size(size.dp)
            .padding(10.dp),
        strokeWidth = width.dp,
        trackColor = Color.Magenta,
    )
}

@Preview(showBackground = true)
@Composable
fun HealthPreview() {
    AppTheme {
        Health(navController = null, paddingValues = PaddingValues(0.dp))
    }
}
