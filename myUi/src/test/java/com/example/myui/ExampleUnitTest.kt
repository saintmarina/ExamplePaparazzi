package com.example.myui

import app.cash.paparazzi.Paparazzi
import org.junit.Test
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MyComponentTest {
    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun myComponentScreenshotTest() {
        paparazzi.snapshot {
            MyComponentPreview()
        }
    }
}