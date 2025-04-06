package com.pa1.logan.Healthcious.database

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.pa1.logan.Healthcious.R
import com.pa1.logan.Healthcious.VM.HealthLog
import com.pa1.logan.Healthcious.VM.Streak
import com.pa1.logan.Healthcious.VM.shoppingCartItem
import com.pa1.logan.Healthcious.ui.composables.MainActivity
import com.pa1.logan.Healthcious.ui.composables.health.getCompactDayDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import kotlin.coroutines.resume
import kotlin.properties.Delegates

class StreakWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val notificationChannelId = "DemoNotificationChannelId"

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val user = getCurrentUser()
        val database = Firebase.database.reference
        createNotificationChannel()

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(0, createNotification())
            }
        }

        return if (user != null) {
            try {
                val updatedStreak = fetchUserStreakSuspend() ?: return Result.retry()
                val userEfList = fetchUserEatenFoodSuspend() ?: return Result.retry()

                var totalCalories = 0f
                for (ef in userEfList) totalCalories += ef.calories

                val userRef = database.child(user.email.toString().substringBefore("@"))
                    .child("streak").child("streak")

                val time = getCompactDayDateTime()

                if (userEfList.isEmpty()) userRef.setValue(Streak(0, updatedStreak.highestStreak))

                else if (updatedStreak.currentStreak > updatedStreak.highestStreak) userRef.setValue(Streak(updatedStreak.currentStreak, updatedStreak.currentStreak))

                else userRef.setValue(updatedStreak).await() // ✅ Ensures Firebase write completes

                database.child(user.email.toString().substringBefore("@")).child("healthlog")
                    .child(time).setValue(HealthLog(time, totalCalories))
                Log.d("StreakWorker", "Streak updated: $updatedStreak")

                deleteUserEatenFood(
                    onResult = { success, message ->
                        Log.d("DeleteEatenFood", "Success: $success, Message: $message")
                    }
                )
                

                Result.success()
            } catch (e: Exception) {
                Log.e("StreakWorker", "Error updating streak", e)
                Result.retry()
            }
        } else {
            Log.d("StreakWorker", "No user, skipping update")
            Result.success()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            0, createNotification()
        )
    }

    private fun createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel(
                notificationChannelId,
                "DemoWorker",
                NotificationManager.IMPORTANCE_DEFAULT,
            )

            val notificationManager: NotificationManager? =
                getSystemService(
                    applicationContext,
                    NotificationManager::class.java)

            notificationManager?.createNotificationChannel(
                notificationChannel
            )
        }
    }

    private fun createNotification() : Notification {

        val mainActivityIntent = Intent(
            applicationContext,
            MainActivity::class.java)

        var pendingIntentFlag by Delegates.notNull<Int>()
        pendingIntentFlag = PendingIntent.FLAG_IMMUTABLE

        val mainActivityPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            mainActivityIntent,
            pendingIntentFlag)


        return NotificationCompat.Builder(
            applicationContext,
            notificationChannelId
        )
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText("Don't lose your streak!")
            .setContentIntent(mainActivityPendingIntent)
            .setAutoCancel(true)
            .build()
    }

    // ✅ Convert `fetchUserStreak` into a suspending function
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun fetchUserStreakSuspend(): Streak? = suspendCancellableCoroutine { cont ->
        fetchUserStreak(
            onDataReceived = { userStreak ->
                val updatedStreak = Streak(userStreak.currentStreak + 1, userStreak.highestStreak)
                cont.resume(updatedStreak) // ✅ Resume coroutine with updated streak
            },
            onFailure = {
                cont.resume(null) // ✅ Resume with null if failed
            }
        )
    }

    private suspend fun fetchUserEatenFoodSuspend(): List<shoppingCartItem>? = suspendCancellableCoroutine { cont ->
        fetchUserEatenFoodOnce(
            onDataReceived = { userEf ->
                cont.resume(userEf) // ✅ Resume coroutine with updated streak
            },
            onFailure = {
                cont.resume(null) // ✅ Resume with null if failed
            }
        )
    }
}

@Composable
fun RuntimePermissionsDialog(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
) {

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        if (ContextCompat.checkSelfPermission(
                LocalContext.current,
                permission) != PackageManager.PERMISSION_GRANTED) {

            val requestLocationPermissionLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->

                    if (isGranted) {
                        onPermissionGranted()
                    } else {
                        onPermissionDenied()
                    }
                }

            SideEffect {
                requestLocationPermissionLauncher.launch(permission)
            }
        }
    }
}
