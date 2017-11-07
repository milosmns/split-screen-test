package com.angrybyte.splitscreentest

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import me.angrybyte.sillyandroid.SillyAndroid
import me.angrybyte.sillyandroid.extras.Coloring
import me.angrybyte.sillyandroid.parsable.Annotations.FindView
import me.angrybyte.sillyandroid.parsable.Annotations.Layout
import me.angrybyte.sillyandroid.parsable.components.ParsableActivity

@Layout(R.layout.activity_main)
class MainActivity : ParsableActivity() {

    @FindView(R.id.activity_main_split_screen_description)
    private lateinit var descriptionView: TextView

    private lateinit var lastConfig: Configuration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // save the default config for later comparison
        lastConfig = Configuration(resources.configuration)
        updateDescription("Activity created")
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        // update description only if screen size changed
        newConfig?.let {
            if (it.diff(lastConfig) and ActivityInfo.CONFIG_SCREEN_SIZE != 0) {
                lastConfig = Configuration(newConfig)
                updateDescription("Config changed")
            }
        }
    }

    /**
     * Checks all UI factors and then updates the [descriptionView]
     * TextView with a description about the app's current state.
     * @param origin Where did the update request originate from
     */
    @SuppressLint("SetTextI18n")
    private fun updateDescription(origin: String) {
        // get current activity info
        val orientationCalc: (Context) -> String = {
            when (it.resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> "portrait"
                Configuration.ORIENTATION_LANDSCAPE -> "landscape"
                Configuration.ORIENTATION_UNDEFINED -> "undefined"
                else -> "unknown"
            }
        }
        val contentView = getContentView<ViewGroup>()
        val activityOrientation = orientationCalc(this)
        val deviceOrientation = orientationCalc(this.applicationContext)
        val isMultiWindowMode = SillyAndroid.UI.isInMultiWindowMode(this)
        val screenMode = if (isMultiWindowMode) "split-screen" else "full-screen"

        // update the description
        descriptionView.text = "Update origin: ${origin.toUpperCase()}\n\n" +
                "Activity is now in $screenMode mode,\n" +
                "$activityOrientation orientation.\n" +
                "Device is in $deviceOrientation orientation mode.\n"

        // change the color on the activity to make sure something is actually changed now
        val colorRandomizer: () -> Int = {
            val randomComponent: () -> Int = { (Math.random() * 255).toInt() }
            Color.rgb(randomComponent(), randomComponent(), randomComponent())
        }
        val backgroundColor = colorRandomizer()
        contentView.setBackgroundColor(backgroundColor)
        descriptionView.setTextColor(Coloring.contrastColor(backgroundColor))

        Log.d("ViewUpdate", "Updated description, origin = $origin")
    }

}
