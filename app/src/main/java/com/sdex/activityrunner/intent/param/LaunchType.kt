package com.sdex.activityrunner.intent.param

import com.sdex.activityrunner.R

object LaunchType {

    private var list: ArrayList<String>? = null
    private val TYPES = object : HashMap<String, Int>() {
        init {
            put("TYPE_ACTIVITY", R.string.launch_param_type_activity)
            put("TYPE_BROADCAST", R.string.launch_param_type_broadcast)
        }
    }

    private fun initList() {
        if (list == null) {
            list = ArrayList(TYPES.keys)
            list!!.sort()
        }
    }

    fun list(): ArrayList<String> {
        initList()
        return list!!
    }

    fun getDisplayTextId(key: String): Int? {
        if(!TYPES.containsKey(key)) return null
        return TYPES.get(key)
    }
}
