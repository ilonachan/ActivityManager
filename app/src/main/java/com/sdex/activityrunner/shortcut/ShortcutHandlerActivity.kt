package com.sdex.activityrunner.shortcut

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.sdex.activityrunner.app.launchActivity
import com.sdex.activityrunner.db.shortcut.Shortcut
import com.sdex.activityrunner.db.shortcut.ShortcutRepository
import com.sdex.activityrunner.db.shortcut.toLaunchParams
import com.sdex.activityrunner.intent.converter.LaunchParamsToIntentConverter
import com.sdex.activityrunner.intent.param.LaunchType
import com.sdex.activityrunner.util.IntentUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ShortcutHandlerActivity : ComponentActivity() {

    @Inject lateinit var shortcutRepository: ShortcutRepository;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val entryId = intent.getLongExtra(ARG_ENTRY_ID, 0);
        if (entryId != 0L) {
            val item = runBlocking { withContext(Dispatchers.IO) {
                    shortcutRepository.getById(entryId)
            }}

            if (item != null) {
                launchComplexIntent(item)
            } else {
                Timber.w("Shortcut: id=$entryId [entry not found]")
            }
        } else {
            val packageName = intent.getStringExtra(ARG_PACKAGE_NAME)
            val className = intent.getStringExtra(ARG_CLASS_NAME)
            if (packageName != null && className != null) {
                launchNamedActivity(packageName, className)
            } else {
                Timber.w("Shortcut: packageName=$packageName, className=$className [not sufficiently specified]")
            }
        }

        finishAffinity()
    }

    private fun launchNamedActivity(packageName: String, className: String) {
        Timber.d("Shortcut: packageName=$packageName, className=$className")
        val componentName = ComponentName(packageName, className)
        // keep it to support shortcuts created before #56
        if (intent.hasExtra(ARG_EXPORTED)) {
            val isExported = intent.getBooleanExtra(ARG_EXPORTED, false)
            launchActivity(componentName, useRoot = !isExported)
        } else {
            val useRoot = intent.getBooleanExtra(ARG_USE_ROOT, false)
            launchActivity(componentName, useRoot = useRoot)
        }
    }

    private fun launchComplexIntent(item: Shortcut) {
        Timber.d("Shortcut: id=${item.id}, name=${item.name}")

        val converter = LaunchParamsToIntentConverter(item.toLaunchParams())
        val intent = converter.convert()

        Timber.d("Shortcut intent: id=${item.id}, intent=${intent}")

        when(item.launchType) {
            LaunchType.Activity, null -> IntentUtils.launchActivity(baseContext, intent)
            LaunchType.Broadcast -> IntentUtils.broadcastIntent(baseContext, intent)
        }
    }

    companion object {

        const val ARG_PACKAGE_NAME = "arg_package_name"
        const val ARG_CLASS_NAME = "arg_class_name"
        const val ARG_EXPORTED = "arg_exported"
        const val ARG_USE_ROOT = "arg_use_root"
        const val ARG_ENTRY_ID = "arg_entry_id"
    }
}
