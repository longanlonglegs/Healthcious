package com.pa1.logan.Healthcious.VM

import com.google.firebase.database.ServerValue
import com.google.firebase.database.ServerValue.TIMESTAMP
import java.time.format.DateTimeFormatter

class HealthVM {

    var healthLogList = mutableListOf<HealthLog>()
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

data class HealthLog(
    val date: String = "01/01/2001",
    val calories: Float = 0f,
)