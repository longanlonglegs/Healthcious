package com.pa1.logan.Healthcious.ui.composables.onboarding

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.database.writeUserGoals

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Onboarding(navController: NavController?) {
    val pages = 3
    val pagerState = rememberPagerState(pageCount = { pages })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        content = { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) { page ->

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    when (page) {
                        0 -> {
                            Page1()
                        }
                        1 -> {
                            Page2()
                        }
                        2 -> {
                            Page3(navController)
                        }
                    }
                }
            }
        },
        bottomBar = {
            LinearProgressIndicator(
                progress = {
                    (pagerState.currentPage.toFloat() + 1) / pagerState.pageCount  // Using currentProgress directly
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp),
            )
        }
    )
}


@Composable
fun Page1() {

    Column(Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        content = {

            Image(painter = painterResource(R.drawable.logo), "Healthcious")

            Text("Welcome to Healthcious!", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 40.sp, textAlign = TextAlign.Center)

            Text("Your one stop paradise to a nutritious life :)", fontSize = 15.sp, textAlign = TextAlign.Center)

        }
    )

}

@Composable
fun Page2() {

    Column(Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        content = {

            Image(painter = painterResource(R.drawable.logo), "Healthcious")

            Text("Surpass your weight loss goals & dreams", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 40.sp, textAlign = TextAlign.Center)

            Text("Using our calorie tracker and visualization tools!", fontSize = 15.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)

            Text("More instructions and info can be found in the info submenu on the main page!", fontSize = 15.sp, textAlign = TextAlign.Center)

        }
    )
}

@Composable
fun Page3(navController: NavController?) {

    var targetCalories by remember { mutableStateOf("") }
    var targetSugar by remember { mutableStateOf("") }
    var targetSalt by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(Modifier
        .fillMaxSize()
        .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        content = {

            Text("Let's first set goals for this journey!", fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, lineHeight = 40.sp, textAlign = TextAlign.Center)
            Text("The average healthy male eats 2000kcal, 20g of sugar and 2000mg of salt", textAlign = TextAlign.Center)

            IconTextField(
                value = targetCalories,
                onValueChange = { targetCalories = it },
                icon = null,
                placeholder = "Target Calories (kCal)"
            )

            IconTextField(
                value = targetSalt,
                onValueChange = { targetSalt = it},
                icon = null,
                placeholder = "Target Sugar (g)"
            )

            IconTextField(
                value = targetSugar,
                onValueChange = { targetSugar = it },
                icon = null,
                placeholder = "Target Salt (mg)"
            )

            Button(onClick = {

                if (targetCalories.length > 7 || targetSugar.length > 7 || targetSalt.length > 7) Toast.makeText(context, "Put a more reasonable number", Toast.LENGTH_SHORT).show()

                else try {
                    val targetCal = targetCalories.toFloat()
                    val targetSug = targetSugar.toFloat()
                    val targetSal = targetSalt.toFloat()

                    if (targetSal > 0 && targetCal > 0 && targetSug > 0)

                    writeUserGoals(
                        Goals(targetCal, targetSug, targetSal, 0, 0L),
                        onResult = {
                            success, message ->
                            if (!success) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            else {
                                Toast.makeText(context, "Goals Set!", Toast.LENGTH_SHORT).show()
                                navController?.navigate("main")
                            }
                        }
                    )

                    else Toast.makeText(context, "Put a more reasonable number", Toast.LENGTH_SHORT).show()
                }

                catch (e: NumberFormatException) {Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()}

            }) {
                Text("Start Journey!")
            }

        }
    )
}

@Composable
fun IconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentDescription: String = "",
    placeholder: String = ""
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
            .height(40.dp)
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                        )
                    }
                    innerTextField()
                }

                if (icon != null) {
                    Icon(
                        icon,
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .requiredSize(48.dp)
                            .padding(16.dp)
                    )
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    AppTheme {
        Page3(navController = null)
    }
}