package com.example.ifeed.ui.theme.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun CustomText(
    modifier: Modifier = Modifier,
    value: String,
    color: Color,
    style: TextStyle
) {
    Text(
        text = value,
        color = color,
        modifier = modifier,
        style = style
    )
}