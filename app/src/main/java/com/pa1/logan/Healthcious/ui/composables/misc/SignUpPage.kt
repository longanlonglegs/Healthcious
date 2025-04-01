package com.pa1.logan.Healthcious.ui.composables.misc

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.signInWithEmail
import com.pa1.logan.Healthcious.database.signUpWithEmail
import com.pa1.logan.Healthcious.database.uploadImg
import com.pa1.logan.Healthcious.database.writeRecipe


@Composable
fun SignUpPage(navController: NavController?) {

    Column(Modifier.fillMaxSize()) {

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.logo),
                "",
                modifier = Modifier.height(200.dp),
                contentScale = ContentScale.Crop
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true,
                supportingText = { Text("Enter your email") })

            TextField(
                value = password,
                onValueChange = { password = it },
                Modifier.fillMaxWidth(),
                label = { Text("Password") },
                singleLine = true,
                supportingText = { Text("Enter your password") })


            Button(onClick = {
                signUpWithEmail("author123@example.com", "password123", { success, message ->
                    Log.d("FirebaseAuth", message ?: "Unknown error")}
                )
                navController?.navigate("main")

            }) {
                Text("Sign In")
            }

            Button(
                onClick = {
                    Toast.makeText(context, "Dish has been deleted!", Toast.LENGTH_SHORT)
                        .show()
                },

                ) {
                Text("Delete")
            }
        }
    }
}