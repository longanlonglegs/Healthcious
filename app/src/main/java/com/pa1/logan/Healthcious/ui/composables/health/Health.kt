package com.pa1.logan.Healthcious.ui.composables.health

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.compose.AppTheme
import com.google.firebase.database.FirebaseDatabase
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphColors
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphFillType
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphStyle
import com.jaikeerthick.composable_graphs.composables.line.style.LineGraphVisibility
import com.jaikeerthick.composable_graphs.style.LabelPosition
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.VM.HealthLog
import com.pa1.logan.Healthcious.VM.ShoppingCartVM
import com.pa1.logan.Healthcious.VM.Streak
import com.pa1.logan.Healthcious.database.StreakWorker
import com.pa1.logan.Healthcious.database.fetchUserEatenFood
import com.pa1.logan.Healthcious.database.fetchUserGoals
import com.pa1.logan.Healthcious.database.fetchUserHealthLog
import com.pa1.logan.Healthcious.database.fetchUserHealthLogContinuous
import com.pa1.logan.Healthcious.database.fetchUserStreak
import com.pa1.logan.Healthcious.database.fetchUserStreakContinuous
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.ui.composables.misc.StarRatingBar
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Health (navController: NavController?) {

    var goal by remember { mutableStateOf(Goals()) }
    val cartVM = remember { ShoppingCartVM() }
    var streak by remember { mutableStateOf(Streak()) }

    val context = LocalContext.current

    var healthStars by remember { mutableFloatStateOf(5f) }
    var currentCalories by remember { mutableFloatStateOf(0f) }
    var currentSugar by remember { mutableFloatStateOf(0f) }
    var currentSalt by remember { mutableFloatStateOf(0f) }

    var progress by remember { mutableFloatStateOf(if (streak.highestStreak == 0) 1f else (streak.currentStreak / streak.highestStreak).toFloat()) }

    val graphStyle = LineGraphStyle(
        visibility = LineGraphVisibility(
            isYAxisLabelVisible = true,
        ),
        yAxisLabelPosition = LabelPosition.LEFT
    )

    var healthData by remember { mutableStateOf(listOf<LineData>()) }

    // Display 10 items
    val pagerState = rememberPagerState(pageCount = {
        3
    })

    val tabList = listOf(
        "Last 3 Days",
        "Last Week",
        "Last Month"
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (getCurrentUser() != null) {
            fetchUserGoals(
                onDataReceived = {
                    goal = it
                    Log.d("User Goal", "Fetched $goal goal!")
                },
                onFailure = { }
            )

            fetchUserEatenFood(
                onDataReceived = {
                    cartVM.shoppingCartList.value = it
                    Log.d("Eaten Food list", "Fetched $it Eaten Food")

                    for (item in cartVM.shoppingCartList.value) {
                        currentCalories += item.calories * item.quantity
                        currentSugar += item.sugar * item.quantity
                        currentSalt += item.salt * item.quantity
                    }

                    healthStars = 5f
                    if (currentCalories > goal.targetCalories) healthStars - 1
                    if (currentSalt > goal.targetSalt) healthStars - 1
                    if (currentSugar > goal.targetSugar) healthStars - 1
                },
                onFailure = { }
            )

            fetchUserStreakContinuous(
                onDataReceived = {
                    streak = it
                    Log.d("User Streak", "Fetched $streak streak!")
                },
                onFailure = { }
            )

            fetchUserHealthLogContinuous(
                onDataReceived = {
                    healthData = emptyList()
                    for (hl in it) healthData += LineData(x = extractDayOfWeek(hl.date), y = hl.calories.toInt())
                    Log.d("User Health Log", "Fetched $healthData healthlog!")
                },
                onFailure = {}
            )
        }

        Log.d("Health", cartVM.shoppingCartList.value.toString())

        Log.d("Health", "Calories: $currentCalories, Sugar: $currentSugar, Salt: $currentSalt")
    }

    LazyColumn(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            {

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                    Column(
                        Modifier
                            .fillMaxWidth(.5f)
                            .padding(10.dp), horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
                    ) {
                        Text(
                            "Current Streak",
                            modifier = Modifier,
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )

                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//                            LinearProgressIndicator(
//                                progress = { progress },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .width(10.dp),
//                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .width(10.dp)
                            )
                        }

                        Text(
                            "Best Streak: ${streak.highestStreak}",
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

                        if (streak.currentStreak < 3) Image(
                                painter = painterResource(R.drawable.orange_fire), "streak",
                                modifier = Modifier.size(100.dp)
                            )
                        else Image(
                            painter = painterResource(R.drawable.blue_fire), "streak",
                            modifier = Modifier.size(100.dp)
                        )
                            Text(
                                "${streak.currentStreak} days",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                    }
                }
            }
        }

        item {
            Text(
                "Progress", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                textAlign = TextAlign.Start, fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 5.dp)
                    .padding(bottom = 5.dp)
            ) {

                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabList.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(title)
                            }
                        )
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {


                        if (healthData.size >= 2) {

                            when (page) {
                                0 ->

                                    LineGraph(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        data = if (healthData.size > 2) healthData.subList(
                                            0,
                                            2
                                        ) else healthData,
                                        onPointClick = { value: LineData ->
                                            Toast.makeText(context, "Date: ${value.x}, Calories: ${value.y}", Toast.LENGTH_SHORT).show()
                                        },
                                        style = graphStyle
                                    )

                                1 ->

                                    LineGraph(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        data = if (healthData.size > 6) healthData.subList(
                                            0,
                                            6
                                        ) else healthData,
                                        onPointClick = { value: LineData ->
                                            Toast.makeText(context, "Date: ${value.x}, Calories: ${value.y}", Toast.LENGTH_SHORT).show()
                                        },
                                        style = graphStyle
                                    )

                                2 ->

                                    LineGraph(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        data = if (healthData.size > 30) healthData.subList(
                                            0,
                                            30
                                        ) else healthData,
                                        onPointClick = { value: LineData ->
                                            Toast.makeText(context, "Date: ${value.x}, Calories: ${value.y}", Toast.LENGTH_SHORT).show()
                                        },
                                        style = graphStyle
                                    )
                            }
                        }

                        else {
                            Text("Track for a few days first!", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
                        }

                    }
                }
            }
        }

        item {
            Text("Health Statistics", modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
                textAlign = TextAlign.Start, fontWeight = FontWeight.Bold)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        20.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    CircularProgressIndicator(
                        progress = {currentCalories / goal.targetCalories},
                        modifier = Modifier
                            .size(100.dp)
                            .padding(10.dp),
                        strokeWidth = 10.dp,
                        trackColor = MaterialTheme.colorScheme.secondaryContainer,
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterVertically
                        )
                    )
                    {

                        Text(
                            "Caloric Intake",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${currentCalories}/${goal.targetCalories}",
                            textAlign = TextAlign.Start,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .padding(5.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                20.dp,
                                Alignment.CenterVertically
                            )
                        ) {

                            Text(
                                "Sugar",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${currentSugar}/${goal.targetSugar}g",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            20.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(
                                20.dp,
                                Alignment.CenterVertically
                            )
                        ) {

                            Text(
                                "Salt",
                                textAlign = TextAlign.Start,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${currentSalt}/${goal.targetSalt}mg",
                                textAlign = TextAlign.Start,
                                fontSize = 15.sp,
                            )
                        }
                    }
                }
            }
        }

//        item {
//
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(5.dp),
//                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
//            )
//            {
//
//                Column(
//                    Modifier
//                        .fillMaxWidth()
//                        .padding(10.dp), horizontalAlignment = Alignment.Start,
//                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
//                ) {
//                    Text(
//                        "Health Rating",
//                        modifier = Modifier.padding(horizontal = 30.dp),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//
//                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
//                        StarRatingBar(
//                            maxStars = 5,
//                            rating = healthStars,
//                            onRatingChanged = {}
//                        )
//                    }
//
//                    Text(
//                        "How is it calculated?",
//                        modifier = Modifier.padding(horizontal = 30.dp),
//                        color = MaterialTheme.colorScheme.secondary
//                    )
//                }
//            }
//
//        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCompactDayDateTime(): String {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.getDefault())
    return today.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun extractDayOfWeek(input: String): String {
    val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.ENGLISH)
    val dateTime = LocalDate.parse(input, formatter)

    // Get day of week from the dateTime object
    return dateTime.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() }
}
