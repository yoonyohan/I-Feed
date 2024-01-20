package com.example.ifeed.ui.theme.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomIcon(
    modifier: Modifier = Modifier,
    iconText: String,
    icon: ImageVector,
    tint: Color
) {
    Icon(
        imageVector = icon,
        contentDescription = iconText,
        tint = tint,
        modifier = modifier
    )
}