package com.angrybyte.splitscreentest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.ColorInt
import android.view.ViewGroup
import android.widget.TextView
import me.angrybyte.sillyandroid.SillyAndroid
import me.angrybyte.sillyandroid.extras.Coloring
import me.angrybyte.sillyandroid.parsable.Annotations.FindView
import me.angrybyte.sillyandroid.parsable.components.ParsableActivity

class MainActivity : ParsableActivity() {

    @Suppress("unused")
    @FindView(R.id.activity_main_split_screen_description)
    private lateinit var descriptionLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateDescription()
    }

    @SuppressLint("SetTextI18n")
    private fun updateDescription() {
        // get current activity info
        val orientationCalc: (Context) -> String = {
            when (it.resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> "portrait"
                Configuration.ORIENTATION_LANDSCAPE -> "landscape"
                Configuration.ORIENTATION_UNDEFINED -> "undefined"
                else -> "unknown"
            }
        }
        val activityOrientation = orientationCalc(this)
        val deviceOrientation = orientationCalc(this.applicationContext)
        val splitScreen = if (SillyAndroid.UI.isInMultiWindowMode(this)) "split-screen" else "full-screen"

        // change the color on the activity to make sure something is actually changed now
        val backgroundColor = randomColor()
        getContentView<ViewGroup>().setBackgroundColor(backgroundColor)
        descriptionLabel.setTextColor(Coloring.contrastColor(backgroundColor))

        // update the description
        descriptionLabel.text = "Activity is now in $splitScreen mode,\n$activityOrientation orientation.\n\nDevice is in $deviceOrientation orientation mode."
    }

    @ColorInt
    private fun randomColor(): Int {
        val randomComponent: () -> Int = { (Math.random() * 255).toInt() }
        return Color.rgb(randomComponent(), randomComponent(), randomComponent())
    }

}
