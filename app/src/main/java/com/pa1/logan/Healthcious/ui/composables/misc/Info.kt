package com.pa1.logan.Healthcious.ui.composables.misc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pa1.logan.Healthcious.ui.composables.recipe.ItemScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Info(navController: NavController?) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Info") },
                navigationIcon = { IconButton(onClick = {
                    navController?.navigate("main")
                }) { Icon(Icons.AutoMirrored.Default.ArrowBack, "go back") } },
            )
        },
        content = {
                paddingValues ->
            Instructions(paddingValues)
        },
    )

}

@Composable
fun Instructions(paddingValues: PaddingValues) {

    LazyColumn(Modifier.fillMaxSize().padding(paddingValues).padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        item {
            Text(
                "Documentation\n" +
                        "2.1\tRecipe and Purchase Page\n" +
                        "        \n" +
                        "It contains a collapsible toolbar with a fun text and a “Hi User..” text area which collapses upon drag input, into something less jarring, allowing users to focus more on the dishes on the page. At the Top App Bar, there is a navigation drawer which contains buttons stored away with little clutter and an info button on the right side, to either log out or to read the documentation for this project.\n" +
                        "There is a search bar below, allowing users to filter their choices. It is made with nice animations and clear text functionality to allow a fun experience for the user. Below the search bar is a lazy column of cards which showcases the different dishes. The dishes consist of both the user’s own one and the app’s default dishes. It is made to look nice even with dark theme on as there is a gradient allowing the text to be seen with ease. It showcases important information to the user on it (etc, the allergen, calories, and cuisine of the dish, and the name) \n" +
                        "There is also a Floating Action Button (FAB) where users can create his/her recipe and purchases. It has a fade and resizing animation which will cause it to disappear when going to the health and eaten food page, which will be further elaborated on later. The different recipes and purchases are all stored under Firebase, where the items are stored as data classes in the real-time database, while the images are stored under cloud storage, allowing everything to sync no matter the device.\n" +
                        "There is also a bottom navigation bar, which contains the 4 pages: Recipe, Purchases, Health, and Eaten Food. The navigation bar can show the selected page and is integrated to sync with the pager, allowing user to be able to use either the navigation bar or swiping to change between pages through a coroutine.\n" +
                        "The difference in the recipes and purchases is that recipes also contain additional information such as ingredients and instructions on how to make the item, while purchases have an additional field of where the item is bought from.\n" +
                        "2.2\tHealth Page\n" +
                        "         \n" +
                        "On the health page, there is a streak card at the top. This tracks whether you have been consistently utilizing the app daily and will reset if the user does not use it. It sends a notification every 24 hours to alert the user not to forget. When the user clicks on the notification, it will cause him/her to go to the app. The flame will turn blue when the user clocks in 3 or more days.\n" +
                        "Below is a graph of the caloric intake over time as that is the most important measurement for a health app and is the best for visualization of progress. When the user logs in for more than 2 days, the graph will be plotted allowing for visualization of progress, where it can be toggled to be the 3 options as shown.\n" +
                        "Then there is the health statistics portion, which visualizes and showcases the various information about your diet thus far through an easy-to-read card view.\n" +
                        "2.3\tEaten Food Page\n" +
                        " \n" +
                        "The eaten food page consists of a card view of all the dishes the user has eaten thus far. It contains the items that, when clicked, will go to the information of each item, as what will happen when you click it under the recipe or purchases page. Each card will also showcase the quantity of what was eaten, to allow for easy tracking.\n" +
                        "2.4\tNavigation Drawer Items\n" +
                        "       \n" +
                        "The navigation drawer contains the account information, settings, and a logout button. The account information will navigate users to a page that allows them to see their email and to change their password. The settings contain a switch to change between light and dark mode, where the icon next to the switch changes dynamically based on the user's input. It also allows users to change their goal target.\n" +
                        "2.5\tCustomize Page\n" +
                        " \n" +
                        "Customize recipe/purchase page shows the first tab where you can choose what category it falls under (purchase or recipe), then put in the relevant information such as image and description. Image is done by calling an intent to the photo browser to select from the pictures you have. \n" +
                        "\n" +
                        "2.6\tItem Screen\n" +
                        " \n" +
                        "It contains the image of the item, together with information about the individual item. The user can choose how much of it they want to eat and click Eat Now to add it to the eaten food list. Once eaten, the process cannot be undone.\n" +
                        "2.7\tOnboarding Screen\n" +
                        " \n" +
                        "This page gives the users a taste of what they can expect when using the app, as well as to set their targets.\n"
            )
        }

    }

}