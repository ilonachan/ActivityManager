package com.sdex.activityrunner.intent.dialog.source

import android.content.Context
import com.sdex.activityrunner.intent.param.LaunchType

class LaunchTypeSource(val context: Context) : SelectionDialogSource {

    override val list = LaunchType.list().map {
        context.getString(LaunchType.getDisplayTextId(it)!!)
    }

    override fun getItem(position: Int): String = list[position]
}
