package com.sdex.activityrunner.db.shortcut

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sdex.activityrunner.db.CategoryListConverter
import com.sdex.activityrunner.intent.LaunchParams
import com.sdex.activityrunner.intent.LaunchParamsExtra
import com.sdex.activityrunner.intent.param.Category
import com.sdex.activityrunner.intent.param.Flag
import com.sdex.activityrunner.intent.param.LaunchType
import java.io.Serializable

@Entity
class Shortcut : Serializable {

    /**
     * Unique database ID, used to identify the intent to be launched from the shortcut
     */
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    /**
     * The moment when the shortcut object was first added
     */
    var timestamp: Long = 0
    /**
     * The display name the user chose for the shortcut
     */
    var name: String? = null
    /**
     * The bitmap image the user chose for the shortcut.
     * This is useful to retain, because it allows restoring
     * shortcut icons upon device backup; if this is not a
     * use case we support, it can be omitted to save space.
     */
    var icon: Bitmap? = null
    /**
     * The package name of the intent to launch, if specified
     */
    var packageName: String? = null
    /**
     * The class name of the intent target activity, if specified
     */
    var className: String? = null
    /**
     * The action of the intent, if specified
     */
    var action: String? = null
    /**
     * Additional data attached to the intent, normally a Uri
     */
    var data: String? = null
    /**
     * The MIME-Type of the [data] attached to the intent, if present
     */
    var mimeType: String? = null
    /**
     * A list of category names (as defined in the `CATEGORY_*` constants of the
     * [Intent][android.content.Intent] class)
     * used by Android to restrict which activities can meaningfully accept the intent,
     * if no specific [ComponentName][android.content.ComponentName] is specified.
     */
    @TypeConverters(CategoryListConverter::class)
    var categories: MutableList<String> = mutableListOf()
    /**
     * A collection of flags (as defined in the `FLAG_*` constants of the
     * [Intent][android.content.Intent] class) modifying the way the intent
     * is to be processed.
     */
    var flags: Int? = null
    /**
     * A mapping of additional data parameters that are passed to the intent receiver.
     */
    var extras: MutableList<LaunchParamsExtra> = mutableListOf()
    /**
     * The way the Intent should be executed: whether Android should select a single activity
     * to start (default), or the Intent should be broadcast to anyone who can receive it.
     */
    var launchType: LaunchType? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Shortcut
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

fun Shortcut.toLaunchParams() =
    LaunchParams().also {
        it.launchType = launchType
        it.action = action
        it.className = className
        it.packageName = packageName
        it.data = data
        it.mimeType = mimeType
        it.extras = ArrayList(extras)
        it.categories = ArrayList(
            categories.mapNotNull {
                Category.byInternal(it)
            })
        it.flags = ArrayList(
            Flag.unpack(flags ?: 0)
        )
    }

fun LaunchParams.toShortcut() =
    Shortcut().also {
        it.launchType = launchType
        it.action = action
        it.className = className
        it.packageName = packageName
        it.data = data
        it.mimeType = mimeType
        it.extras = extras
        it.categories = ArrayList(categories.map { Category.list()[it] })
        it.flags = flags.fold(0) { acc, new -> acc or Flag.at(new) }
    }
