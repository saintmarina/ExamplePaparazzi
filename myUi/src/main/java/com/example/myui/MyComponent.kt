package com.example.myui

@Composable
fun MyComponent() {
    Column {
        Text("Hello World")
    }
}

@Preview
@Composable
fun MyComponentPreview() {
    AppTheme {
        MyComponent()
    }
}