package com.sdex.activityrunner.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.sdex.activityrunner.intent.LaunchParamsExtra
import com.sdex.activityrunner.intent.converter.ExtrasSerializer
import com.sdex.activityrunner.intent.converter.IntegerListSerializer
import com.sdex.activityrunner.intent.param.LaunchType
import java.io.ByteArrayOutputStream

fun Bitmap.toBytes(): ByteArray = ByteArrayOutputStream().also {
    compress(Bitmap.CompressFormat.PNG, 0 /*ignored*/, it)
}.toByteArray()

class BitmapConverter {
    @TypeConverter fun bmpToBlob(bmp: Bitmap) = bmp.toBytes()
    @TypeConverter fun bmpFromBlob(blob: ByteArray): Bitmap? =
        BitmapFactory.decodeByteArray(blob, 0, blob.size)
}

class LaunchTypeConverter {
    @TypeConverter fun enumToString(enum: LaunchType) = enum.name
    @TypeConverter fun enumFromString(str: String) = LaunchType.valueOfOrNull(str)
}

class IntListConverter(
    private val ser: IntegerListSerializer = IntegerListSerializer()
) {
    @TypeConverter fun listToString(l: List<Int>?) = l?.let { ser.serialize(it) }
    @TypeConverter fun listFromString(s: String?) = ser.deserialize(s)
}

class ExtrasConverter(
    private val ser: ExtrasSerializer = ExtrasSerializer()
) {
    @TypeConverter fun extrasToString(e: List<LaunchParamsExtra>?) = e?.let { ser.serialize(it) }
    @TypeConverter fun extrasFromString(s: String?) = ser.deserialize(s)
}

class CategoryListConverter {
    @TypeConverter fun categoriesToString(c: MutableList<String>?) = c?.let { c.joinToString("|") }
    @TypeConverter fun categoriesFromString(s: String?) = s?.split('|')?.toMutableList()
}

class UriConverter {
    @TypeConverter fun uriToString(u: Uri) = u.toString()
    @TypeConverter fun uriFromString(s: String?) = s?.toUri()
}
