package com.sdex.activityrunner.shortcut

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.content.pm.ShortcutManagerCompat.FLAG_MATCH_PINNED
import androidx.core.graphics.drawable.IconCompat
import com.sdex.activityrunner.R
import com.sdex.activityrunner.db.shortcut.Shortcut
import com.sdex.activityrunner.db.shortcut.ShortcutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date

fun listShortcuts(context: Context): List<ShortcutInfoCompat> =
    ShortcutManagerCompat.getShortcuts(context, FLAG_MATCH_PINNED)

private const val CLEANUP_ITEMS_MAX_LOG = 10

fun cleanupShortcuts(context: Context, shortcutRepository: ShortcutRepository) {
    shortcutRepository.clearUnusedAndGet(listShortcuts(context).mapNotNull {
        it.intent.getLongExtra(ShortcutHandlerActivity.ARG_ENTRY_ID, 0).takeIf { it != 0L }
    }).also { items ->
        if(items.isEmpty()) {
            Timber.i("Deleted 0 stale shortcut entries from DB")
        } else {
            Timber.i(
                "Deleted ${items.size} stale shortcut entries from DB: ${
                    items.mapIndexed { index, it ->
                        if (index < CLEANUP_ITEMS_MAX_LOG) "\"${it.name}\" (${it.id})" else "..."
                    }.take(CLEANUP_ITEMS_MAX_LOG + 1).joinToString(", ")
                }",
            )
        }
    }
}

fun createShortcut(context: Context, shortcut: Shortcut,
                            shortcutRepository: ShortcutRepository): Boolean {
    runBlocking { withContext(Dispatchers.IO) {
        shortcut.timestamp = Date().time
        shortcutRepository.insert(shortcut).also { shortcut.id = it }
    }}

    val wrappingIntent = Intent(context, ShortcutHandlerActivity::class.java)
    wrappingIntent.putExtra(ShortcutHandlerActivity.ARG_ENTRY_ID, shortcut.id)
    wrappingIntent.action = "android.intent.action.MAIN"
    return createShortcut(context, shortcut.name!!, wrappingIntent, shortcut.icon)
}

fun createShortcut(context: Context, name: String, intent: Intent, icon: Bitmap?): Boolean {
    val iconCompat = if (icon != null) {
        IconCompat.createWithBitmap(icon)
    } else {
        IconCompat.createWithResource(context, R.mipmap.ic_launcher)
    }
    return createShortcut(context, name, intent, iconCompat)
}

fun createShortcut(context: Context, name: String, intent: Intent, icon: IconCompat): Boolean {
    if (ShortcutManagerCompat.isRequestPinShortcutSupported(context)) {
        val pinShortcutInfo = ShortcutInfoCompat.Builder(context, name)
            .setIcon(icon)
            .setShortLabel(name)
            .setIntent(intent)
            .build()
        return ShortcutManagerCompat.requestPinShortcut(context, pinShortcutInfo, null)
    }
    return false
}
