package com.example.ifeed.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CustomFilledButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClickAction: () -> Unit
) {
    Button(
        onClick = { onClickAction() },
        modifier = modifier
            .fillMaxWidth()
    ) {
        CustomText(
            value = buttonText,
            modifier = modifier,
            color = Color.Black,
            style = TextStyle(
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}