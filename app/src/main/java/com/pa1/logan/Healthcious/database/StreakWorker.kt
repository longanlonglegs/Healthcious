package com.pa1.logan.Healthcious.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pa1.logan.Healthcious.VM.Streak

class StreakWorker(
    private val appContext: Context,
    params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        var streak = Streak()

        fetchUserStreak(
            onDataReceived = {
                streak = it
                Log.d("demoworker", "Fetched $streak streak! on health page")

                val newStreak = Streak(streak.currentStreak + 1, streak.highestStreak)

                writeUserStreak(newStreak,
                    onResult = { success, message ->
                        Log.d("demoworker", "success: $success")
                    }
                )
            },
            onFailure = {}
        )

        deleteUserEatenFood(
            onResult = { success, message ->
                Log.d("demoworker", "deleted the user eaten food: $success")
            }
        )

        Log.d("demoworker", "do work done!")

        return Result.success()
    }
}
