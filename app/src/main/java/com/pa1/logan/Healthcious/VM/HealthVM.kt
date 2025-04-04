package com.pa1.logan.Healthcious.VM

import com.google.firebase.database.ServerValue
import com.google.firebase.database.ServerValue.TIMESTAMP

class HealthVM {

    var totalCalories = 0f
    var totalSugar = 0f
    var totalSalt = 0f

}

data class Goals(
    val targetCalories: Float = 2000f,
    val targetSugar: Float = 30f,
    val targetSalt: Float = 2000f,
    val streak: Int = 0,
    val elapsedTIme: Long = 0L,
)

data class Streak(
    val currentStreak: Int = 0,
    val highestStreak: Int = 0,
)