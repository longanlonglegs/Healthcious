package com.pa1.logan.Healthcious.ui.composables.health

import androidx.compose.ui.graphics.Path
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose.AppTheme
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.VM.HealthVM
import com.pa1.logan.Healthcious.VM.ShoppingCartVM
import com.pa1.logan.Healthcious.database.fetchPurchases
import com.pa1.logan.Healthcious.database.fetchUserEatenFood
import com.pa1.logan.Healthcious.database.fetchUserGoals
import com.pa1.logan.Healthcious.database.fetchUserPurchases
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.ui.composables.misc.StarRatingBar
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Health (navController: NavController?, paddingValues: PaddingValues) {

    var goal by remember { mutableStateOf(Goals()) }
    val cartVM = remember { ShoppingCartVM() }

    var healthStars by remember { mutableFloatStateOf(5f) }
    var currentCalories by remember { mutableFloatStateOf(0f) }
    var currentSugar by remember { mutableFloatStateOf(0f) }
    var currentSalt by remember { mutableFloatStateOf(0f) }

    val data = listOf(LineData(x = "Sun", y = 200), LineData(x = "Mon", y = 40))

    LaunchedEffect(Unit) {
        if (getCurrentUser() != null) fetchUserGoals(
            onDataReceived = {
                goal = it
                Log.d("User Goal", "Fetched $goal goal!")
            },
            onFailure = { }
        )

        if (getCurrentUser() != null) fetchUserEatenFood(
            onDataReceived = {
                cartVM.shoppingCartList.value = it
                Log.d("Eaten Food list", "Fetched $it Eaten Food")

                for (item in cartVM.shoppingCartList.value) {
                    currentCalories += item.calories * item.quantity
                    currentSugar += item.sugar * item.quantity
                    currentSalt += item.salt * item.quantity
                }
            },
            onFailure = { }
        )

        Log.d("Health", cartVM.shoppingCartList.value.toString())

        Log.d("Health", "Calories: $currentCalories, Sugar: $currentSugar, Salt: $currentSalt")
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
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
                            LinearProgressIndicator(
                                progress = { 0.5f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .width(10.dp),
                                trackColor = MaterialTheme.colorScheme.primaryContainer,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Text(
                            "Best Streak: XX Days",
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.orange_fire), "streak",
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            "1 Day",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        item {

            Text("Progress", modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
                textAlign = TextAlign.Start, fontWeight = FontWeight.Bold)

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {

                // Display 10 items
                val pagerState = rememberPagerState(pageCount = {
                    3
                })

                val tabList = listOf(
                    "Today",
                    "Last 3 Days",
                    "Last Week"
                )
                val scope = rememberCoroutineScope()

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
                                when (pagerState.currentPage) {
                                    0 -> Text(text = title)
                                    1 -> Text(text = title)
                                    2 -> Text(text = title)
                                }
                            }
                        )
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
                            when(page) {
                                0 ->
                                LineGraph(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    data = data,
                                    onPointClick = { value: LineData ->
                                        // do something with value
                                    },
                                )
                                1 -> Text(text = "Last 3 Days")
                                2 -> Text(text = "Last Week")
                            }
                        }
                    }
                }
            }
        }


        item {

            Text("Health Statistics", modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
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

                    ProgressCircle(currentCalories / goal.targetCalories, 100, 10)

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
            Row(Modifier.fillMaxWidth()) {

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

        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
            )
            {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp), horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        "Health Rating",
                        modifier = Modifier.padding(horizontal = 30.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp
                    )

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        StarRatingBar(
                            maxStars = 5,
                            rating = healthStars,
                            onRatingChanged = { healthStars = it }
                        )
                    }

                    Text(
                        "How is it calculated?",
                        modifier = Modifier.padding(horizontal = 30.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

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
        trackColor = MaterialTheme.colorScheme.secondary,
    )
}

@Preview(showBackground = true)
@Composable
fun HealthPreview() {
    AppTheme {
        Health(navController = null, paddingValues = PaddingValues(0.dp))
    }
}
