package com.pa1.logan.Healthcious.ui.composables

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.RunningWithErrors
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.WorkManager
import com.example.compose.AppTheme
import com.jaikeerthick.composable_graphs.composables.line.LineGraph
import com.jaikeerthick.composable_graphs.composables.line.model.LineData
import com.pa1.logan.Healthcious.VM.Goals
import com.pa1.logan.Healthcious.VM.Streak
import com.pa1.logan.Healthcious.database.getCurrentUser
import com.pa1.logan.Healthcious.database.signOut
import com.pa1.logan.Healthcious.database.writeUserGoals
import com.pa1.logan.Healthcious.database.writeUserStreak
import com.pa1.logan.Healthcious.ui.composables.health.Health
import com.pa1.logan.Healthcious.ui.composables.misc.MyCart
import com.pa1.logan.Healthcious.ui.composables.purchase.PurchaseList
import com.pa1.logan.Healthcious.ui.composables.recipe.RecipeList
import kotlinx.coroutines.launch
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pa1.logan.Healthcious.database.RuntimePermissionsDialog

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController?) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val context = LocalContext.current

    val pagerState = rememberPagerState(pageCount = {
        4
    })

    val scope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    var permissionRequested by remember { mutableStateOf(false) }

    if (!permissionRequested) {
        RuntimePermissionsDialog(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionGranted = {
                permissionRequested = true
            },
            onPermissionDenied = {
                permissionRequested = true
            }
        )
    }

    var subtitle by remember { mutableStateOf(listOf("Ready to cook?", "Ready to buy?", "Ready to stay healthy?", "Ready to track food eaten?")) }
    var appTitle by remember { mutableStateOf(listOf("Recipe", "Purchase", "Health", "Eaten Food")) }
    var userGreeting by remember { mutableStateOf("Hi ${getCurrentUser()?.email.toString().substringBefore("@")}!") }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("Healthcious", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("Account Information") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = null) },
                        onClick = {
                            navController?.navigate("account")
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        onClick = {
                            navController?.navigate("settings")
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Log Out") },
                        selected = false,
                        icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                        onClick = {
                            signOut()
                            Toast.makeText(navController?.context, "Logged out", Toast.LENGTH_SHORT).show()
                            navController?.navigate("signin")
                        },
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {

        Scaffold(
            topBar = { LargeTopAppBar(
                title = {

                    val collapsedFraction = scrollBehavior.state.collapsedFraction

                    if (collapsedFraction < 0.5f) { // only show when expanded
                        Column{

                            Text(
                                subtitle[pagerState.currentPage],
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )


                            Text(
                                userGreeting,
                                modifier = Modifier.fillMaxWidth(),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    else{

                        Text(
                            appTitle[pagerState.currentPage],
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                                .padding(bottom = 5.dp),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp,
                            lineHeight = 30.sp
                        )
                    }

                },

                scrollBehavior = scrollBehavior,

                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }

                },

                actions = {
                    MinimalDropdownMenu(navController)
                }
            )
            },

            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

            content = {
                    paddingValues ->

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) { page ->

                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            when(page) {
                                0 -> RecipeList(navController)
                                1 -> PurchaseList(navController)
                                2 -> Health(navController)
                                3 -> MyCart(navController)
                            }
                        }
                    }
                }
            },

            floatingActionButton = {
                AnimatedVisibility(
                    visible = pagerState.currentPage == 0 || pagerState.currentPage == 1,
                    enter = fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300))
                ) {
                    FloatingActionButton(
                        onClick = {
                            navController?.navigate("customize")
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add own dish")
                    }
                }
            },

            bottomBar = {
                NavigationBar (modifier = Modifier.height(80.dp), containerColor = Color.Transparent){
                    NavigationBarItem(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        icon = { Icon(Icons.Default.Book, "Recipe")},
                        selected = pagerState.currentPage == 0,
                    )
                    NavigationBarItem(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        icon = { Icon(Icons.Default.Storefront, "Purchase")},
                        selected = pagerState.currentPage == 1,
                    )
                    NavigationBarItem(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        },
                        icon = { Icon(Icons.Default.RunningWithErrors, "Health")},
                        selected = pagerState.currentPage == 2,
                    )
                    NavigationBarItem(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(3)
                            }
                        },
                        icon = { Icon(Icons.Default.Fastfood, "Shopping Cart")},
                        selected = pagerState.currentPage == 3,
                    )
                }
            }
        )

    }

}

@Composable
fun MinimalDropdownMenu(navController: NavController?) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.Info, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("More Info") },
                onClick = {
                    navController?.navigate("info")
                }
            )
            DropdownMenuItem(
                text = { Text("Logout") },
                onClick = {
                    signOut()
                    Toast.makeText(navController?.context, "Logged out", Toast.LENGTH_SHORT).show()
                    navController?.navigate("signin")
                }
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    AppTheme {
        MainPage(navController = null)
    }
}

