package com.sdex.activityrunner

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sdex.activityrunner.preferences.AppPreferences
import com.sdex.activityrunner.shortcut.ShortcutCleanupWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class ActivityManagerApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            Timber.plant(Timber.DebugTree())
        }
        appPreferences.onAppOpened()
        AppCompatDelegate.setDefaultNightMode(appPreferences.theme)

        val workRequest = PeriodicWorkRequestBuilder<ShortcutCleanupWorker>(24, TimeUnit.HOURS).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            ShortcutCleanupWorker.UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()
}
