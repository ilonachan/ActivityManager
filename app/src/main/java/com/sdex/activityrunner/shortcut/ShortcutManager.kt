package com.sdex.activityrunner.shortcut

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.sdex.activityrunner.R
import com.sdex.activityrunner.intent.IntentBroadcastActivity

fun createBroadcastShortcut(context: Context, name: String, historyId: Int, icon: Bitmap?): Boolean {
    val wrappingIntent = Intent(context, IntentBroadcastActivity::class.java)
    wrappingIntent.putExtra("broadcast", historyId)
    wrappingIntent.action = "android.intent.action.MAIN"
    return createShortcut(context, name, wrappingIntent, icon)
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
