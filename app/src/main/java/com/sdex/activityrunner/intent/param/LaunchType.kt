package com.sdex.activityrunner.intent.param

import com.sdex.activityrunner.R

/**
 * Describes the different ways in which an Intent can be fired.
 * This class also defines the way these options appear in the
 * [IntentBuilderActivity][com.sdex.activityrunner.intent.IntentBuilderActivity].
 */
enum class LaunchType(
    val displayResource: Int
) {
    /**
     * Android should select a single matching activity to be executed within the same task
     */
    Activity(R.string.launch_param_type_activity),
    /**
     * The Intent should be broadcast to anyone who may receive it
     */
    Broadcast(R.string.launch_param_type_broadcast);

    companion object {
        fun valueOfOrNull(key: String) =
            try { valueOf(key) } catch (_: IllegalArgumentException) { null }
    }
}
