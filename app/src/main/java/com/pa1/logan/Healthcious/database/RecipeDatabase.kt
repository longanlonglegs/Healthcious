package com.pa1.logan.Healthcious.database

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.pa1.logan.Healthcious.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import coil.compose.rememberAsyncImagePainter


fun uploadImg(name: String, imageUri: Uri, onUploadSuccess: (String) -> Unit) {
   val storageRef = FirebaseStorage.getInstance().reference
   val fileName = "images/${name}.png"
   val imageRef = storageRef.child(fileName)

   imageRef.putFile(imageUri)
      .addOnSuccessListener {
         imageRef.downloadUrl.addOnSuccessListener { url ->
            onUploadSuccess(url.toString()) // Pass URL back to Composable
         }
      }
      .addOnFailureListener {
         println("Upload failed: ${it.message}")
      }
}

fun getDownloadUrl(imagePath: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
   val storageRef = FirebaseStorage.getInstance().reference.child(imagePath)

   storageRef.downloadUrl
      .addOnSuccessListener { uri ->
         onSuccess(uri.toString())  // Get the direct image URL
      }
      .addOnFailureListener { exception ->
         Log.e("Firebase", "Error getting image URL: ${exception.message}")
         onFailure(exception)
      }
}

@Composable
fun showImg(imagePath: String): Painter {

   var imageUrl by remember { mutableStateOf<String?>(null) }
   val context = LocalContext.current


   getDownloadUrl(imagePath,
      onSuccess = { url -> imageUrl = url },
      onFailure = {
         Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
      }
   )

//   imageUrl?.let {
//      Image(
//         painter = rememberAsyncImagePainter(it),
//         contentDescription = "Firebase Image",
//         modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .drawWithContent {
//               drawContent()
//               drawRect(
//                  brush = Brush.verticalGradient(
//                     colors = listOf(
//                        Color.Transparent, // Fully transparent at the bottom
//                        Color.Black // Light transparency at the top
//                     )
//                  )
//               )
//            },
//         contentScale = ContentScale.Crop,
//
//      )
//   }

   return rememberAsyncImagePainter(model = imageUrl)
}
