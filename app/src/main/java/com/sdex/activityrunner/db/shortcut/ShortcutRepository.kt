package com.sdex.activityrunner.db.shortcut

import javax.inject.Inject

class ShortcutRepository @Inject constructor(
    private val shortcutDao: ShortcutDao
) {

    fun findAllUnused(ids: List<Long>) = shortcutDao.findAllUnused(ids)

    fun clearUnused(ids: List<Long>) = shortcutDao.clearUnused(ids)

    fun clearUnusedAndGet(ids: List<Long>) = shortcutDao.clearUnusedAndGet(ids)

    fun getById(id: Long) = shortcutDao.getById(id)

    fun insert(model: Shortcut) = shortcutDao.insert(model)

    fun update(model: Shortcut) = shortcutDao.update(model)

    fun delete(model: Shortcut) = shortcutDao.delete(model)

}
