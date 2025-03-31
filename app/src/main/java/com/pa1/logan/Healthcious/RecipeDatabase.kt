package com.pa1.logan.Healthcious

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

fun RecipeDatabase() {

    val db = Firebase.firestore

    // Create a new user with a first and last name
    val user = hashMapOf(
        "first" to "Ada",
        "last" to "Lovelace",
        "born" to 1815
    )

// Add a new document with a generated ID
    db.collection("users")
        .add(user)
        .addOnSuccessListener { documentReference ->
            Log.d("works", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("error", "Error adding document", e)
        }

    db.collection("users")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                Log.d("works", "${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.w("error", "Error getting documents.", exception)
        }
}
