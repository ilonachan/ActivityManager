package com.sdex.activityrunner.shortcut

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sdex.activityrunner.db.shortcut.ShortcutRepository
import com.sdex.activityrunner.util.work.DebugWorkTrigger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@HiltWorker
class ShortcutCleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context, @Assisted workerParams: WorkerParameters,
    val shortcutRepository: ShortcutRepository
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Timber.i("Begin periodic cleanup of shortcuts")
        cleanupShortcuts(applicationContext, shortcutRepository)
        Timber.i("Periodic cleanup of shortcuts complete")

        return Result.success()
    }

    companion object {
        val UNIQUE_WORK_NAME = "com.sdex.activitymanager.shortcut.ShortcutCleanupWorker"
    }

    @AndroidEntryPoint
    class Trigger() : DebugWorkTrigger(ShortcutCleanupWorker::class.java)
}
