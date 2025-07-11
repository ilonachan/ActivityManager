package com.sdex.activityrunner.intent.param

import com.sdex.activityrunner.R

enum class LaunchType(
    val displayResource: Int
) {
    Activity(R.string.launch_param_type_activity),
    Broadcast(R.string.launch_param_type_broadcast);

    companion object {
        fun valueOfOrNull(key: String) =
            try { valueOf(key) } catch (_: IllegalArgumentException) { null }
    }
}
