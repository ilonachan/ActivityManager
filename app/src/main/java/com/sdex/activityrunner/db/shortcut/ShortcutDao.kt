package com.sdex.activityrunner.db.shortcut

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
abstract class ShortcutDao {

    @Query("SELECT * FROM Shortcut WHERE id NOT IN (:ids)")
    abstract fun findAllUnused(ids: List<Long>): List<Shortcut>

    @Query("DELETE FROM Shortcut WHERE id NOT IN (:ids)")
    abstract fun clearUnused(ids: List<Long>)

    @Transaction
    open fun clearUnusedAndGet(ids: List<Long>) =
        findAllUnused(ids).also { delete(*it.toTypedArray()) }

    @Query("SELECT * FROM Shortcut WHERE id = :id")
    abstract fun getById(id: Long): Shortcut?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(model: Shortcut): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(vararg model: Shortcut)

    @Delete
    abstract fun delete(vararg model: Shortcut)
}
