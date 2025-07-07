package com.sdex.activityrunner.intent

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.sdex.activityrunner.R
import com.sdex.activityrunner.commons.BaseActivity
import com.sdex.activityrunner.intent.converter.HistoryToLaunchParamsConverter
import com.sdex.activityrunner.intent.converter.LaunchParamsToIntentConverter
import com.sdex.activityrunner.intent.history.HistoryViewModel
import com.sdex.activityrunner.util.IntentUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class IntentBroadcastActivity : BaseActivity() {

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val historyId = intent.getIntExtra("broadcast", 0)
        if (historyId == 0) {
            Toast.makeText(baseContext, R.string.starting_intent_broadcast_failed, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModel.list.observe(this) { list ->
            val historyModel = list.find { it.id == historyId } ?: run {
                Toast.makeText(baseContext, R.string.starting_intent_broadcast_failed, Toast.LENGTH_SHORT).show()
                finish()
                return@observe
            }
            val historyToLaunchParamsConverter = HistoryToLaunchParamsConverter(historyModel)
            val launchParams = historyToLaunchParamsConverter.convert()
            val converter = LaunchParamsToIntentConverter(launchParams)
            val intent = converter.convert()

            if(launchParams.launchType == "TYPE_BROADCAST")
                IntentUtils.broadcastIntent(baseContext, intent)
            else //just for completeness
                IntentUtils.launchActivity(baseContext, intent)
            finish()
        }
    }
}
