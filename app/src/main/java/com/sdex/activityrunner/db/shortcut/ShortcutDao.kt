package com.sdex.activityrunner.db.shortcut

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShortcutDao {

    @Query("SELECT * FROM Shortcut WHERE id NOT IN (:ids)")
    fun findAllUnused(ids: List<Long>): List<Shortcut>

    @Query("DELETE FROM Shortcut WHERE id NOT IN (:ids)")
    fun clearUnused(ids: List<Long>)

    @Query("SELECT * FROM Shortcut WHERE id = :id")
    fun getById(id: Long): Shortcut?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: Shortcut): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(model: Shortcut)

    @Delete
    fun delete(model: Shortcut)
}
