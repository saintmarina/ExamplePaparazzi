package com.example.myui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun MyComponent() {
    Column {
        Text(
            text = "Hello World",
            fontSize = 100.sp,
        )
    }
}

@Preview
@Composable
fun MyComponentPreview() {
    MyComponent()
}