package com.example.myui

import app.cash.paparazzi.Paparazzi
import org.junit.Test
import org.junit.Rule

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