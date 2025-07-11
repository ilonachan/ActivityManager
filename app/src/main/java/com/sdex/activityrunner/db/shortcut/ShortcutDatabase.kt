package com.sdex.activityrunner.db.shortcut

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.sdex.activityrunner.db.BitmapConverter
import com.sdex.activityrunner.db.ExtrasConverter
import com.sdex.activityrunner.db.IntListConverter
import com.sdex.activityrunner.db.LaunchTypeConverter
import com.sdex.activityrunner.db.UriConverter

@TypeConverters(
    BitmapConverter::class, LaunchTypeConverter::class,
    IntListConverter::class, ExtrasConverter::class, UriConverter::class)
@Database(
    entities = [(Shortcut::class)],
    version = 1,
    exportSchema = true,
)
abstract class ShortcutDatabase : RoomDatabase() {

    abstract val shortcutDao: ShortcutDao

    companion object {

        private const val DB_NAME = "shortcut.db"
        private var database: ShortcutDatabase? = null

        fun getDatabase(context: Context): ShortcutDatabase {
            if (database == null) {
                database = Room.databaseBuilder(context, ShortcutDatabase::class.java, DB_NAME)
                    .build()
            }
            return database!!
        }
    }
}
