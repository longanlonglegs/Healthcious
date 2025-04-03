package com.pa1.logan.Healthcious.ui.composables.misc

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.compose.AppTheme
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.Recipe
import com.pa1.logan.Healthcious.database.signInWithEmail
import com.pa1.logan.Healthcious.database.signUpWithEmail
import com.pa1.logan.Healthcious.database.writeRecipe
import com.pa1.logan.Healthcious.ui.composables.MainPage


@Composable
fun SignInPage(navController: NavController?) {

    var email = remember { TextFieldState() }
    val context = LocalContext.current
    val password = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()){

        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {


            Text("Welcome Back", fontSize = 40.sp,  fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), textAlign = TextAlign.Center)
            Text("Login to your account", modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(10.dp))

            BasicTextField(
                state = email,
                lineLimits = TextFieldLineLimits.SingleLine,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                    .padding(6.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                decorator = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 48.dp, end = 48.dp)
                        ) {
                            innerTextField()
                        }

                        Icon(
                            Icons.Filled.Email,
                            contentDescription = "Password",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .requiredSize(48.dp).padding(16.dp)
                        )
                    }
                }
            )

            BasicSecureTextField(
                state = password,
                textObfuscationMode =
                if (showPassword) {
                    TextObfuscationMode.Visible
                } else {
                    TextObfuscationMode.RevealLastTyped
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                    .padding(6.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                decorator = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 48.dp, end = 48.dp)
                        ) {
                            innerTextField()
                        }
                        Icon(
                            if (showPassword) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = "Toggle password visibility",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .requiredSize(48.dp).padding(16.dp)
                                .clickable { showPassword = !showPassword }
                        )
                        Icon(
                            Icons.Filled.Key,
                            contentDescription = "Password",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .requiredSize(48.dp).padding(16.dp)
                        )
                    }
                }
            )


            Button(onClick = {
                signInWithEmail(
                    email.text.toString(),
                    password.text.toString(),
                    { success, message ->
                        Log.d("FirebaseAuth", message ?: "Unknown error")
                        if (!success) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        else {
                            Toast.makeText(context, "Signed In!", Toast.LENGTH_SHORT).show()
                            navController?.navigate("main")
                        }
                    }
                )

            }) {
                Text("Sign In")
            }

            Text("New User? Click here to sign up!",
                color = Color.Magenta,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(
                    onClick = {
                        navController?.navigate("signup")
                    }
                ))
        }
    }
}

@Composable
fun SignUpPage(navController: NavController?) {

    var email = remember { TextFieldState() }
    val context = LocalContext.current
    val password = remember { TextFieldState() }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text("Hi there", fontSize = 100.sp)

        Spacer(modifier = Modifier.height(20.dp))

        BasicTextField(
            state = email,
            lineLimits = TextFieldLineLimits.SingleLine,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                .padding(6.dp)
                .height(40.dp),
            decorator = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 48.dp, end = 48.dp)
                    ) {
                        innerTextField()
                    }

                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Password",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .requiredSize(48.dp).padding(16.dp)
                    )
                }
            }
        )

        BasicSecureTextField(
            state = password,
            textObfuscationMode =
            if (showPassword) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.RevealLastTyped
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp))
                .padding(6.dp)
                .height(40.dp),
            decorator = { innerTextField ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 48.dp, end = 48.dp)
                    ) {
                        innerTextField()
                    }
                    Icon(
                        if (showPassword) {
                            Icons.Filled.Visibility
                        } else {
                            Icons.Filled.VisibilityOff
                        },
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .requiredSize(48.dp).padding(16.dp)
                            .clickable { showPassword = !showPassword }
                    )
                    Icon(
                        Icons.Filled.Key,
                        contentDescription = "Password",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .requiredSize(48.dp).padding(16.dp)
                    )
                }
            }
        )

        Button(onClick = {
            signUpWithEmail(email.text.toString(), password.text.toString(), { success, message ->
                Log.d("FirebaseAuth", message ?: "Unknown error")
                if (!success) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                else {
                    Toast.makeText(context, "Signed Up!", Toast.LENGTH_SHORT).show()
                    navController?.navigate("main")
                }
            }
            )

        }) {
            Text("Sign Up")
        }

        Text("Existing User? Click here to sign in!",
            color = Color.Blue,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(
                onClick = {
                    navController?.navigate("signin")
                }
            ))
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpPagePreview() {
    AppTheme {
        SignUpPage(navController = null)
    }
}
