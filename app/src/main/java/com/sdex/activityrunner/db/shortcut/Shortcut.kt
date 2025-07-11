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
    var mimeType: String? = null
    @TypeConverters(CategoryListConverter::class)
    var categories: MutableList<String> = mutableListOf()
    var flags: Int? = null
    var extras: MutableList<LaunchParamsExtra> = mutableListOf()
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
