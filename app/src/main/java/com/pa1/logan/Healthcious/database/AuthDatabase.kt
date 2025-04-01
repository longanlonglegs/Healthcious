package com.pa1.logan.Healthcious.database

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun signInWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                onResult(true, "Signed in as: ${user?.email}")
            } else {
                onResult(false, task.exception?.message)
            }
        }
}

fun signUpWithEmail(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                onResult(true, "User created: ${user?.email}")
            } else {
                onResult(false, task.exception?.message)
            }
        }
}

fun getCurrentUser(): FirebaseUser? {
    return FirebaseAuth.getInstance().currentUser
}

fun signOut() {
    FirebaseAuth.getInstance().signOut()
}