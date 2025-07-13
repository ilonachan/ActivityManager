package com.sdex.activityrunner.util.work

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.await
import kotlinx.coroutines.launch
import timber.log.Timber

open class DebugWorkTrigger(
    val workerClass: Class<out ListenableWorker>
) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("Immediately enqueueing ${workerClass.simpleName}")

        val req = OneTimeWorkRequest.Builder(workerClass).build()

        val op = WorkManager.getInstance(applicationContext).enqueue(req)

        lifecycleScope.launch {
            try {
                op.await()
                Timber.i("${workerClass.simpleName} succeeded")
            } catch (e: Exception) {
                Timber.e(e, "${workerClass.simpleName} failed")
            } finally {
                finish()
            }
        }
    }
}
