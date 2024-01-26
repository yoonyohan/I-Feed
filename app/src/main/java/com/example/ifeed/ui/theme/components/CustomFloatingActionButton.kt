package com.example.ifeed.ui.theme.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconText: String,
    onCallAction: () -> Unit,
) {
    FloatingActionButton(
        content = {
               CustomIcon(
                   iconText = iconText,
                   icon = icon,
                   tint = MaterialTheme.colorScheme.background
               )
        },
        onClick = { onCallAction()},
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}