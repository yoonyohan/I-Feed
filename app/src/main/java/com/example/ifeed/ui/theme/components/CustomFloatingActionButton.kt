package com.example.ifeed.ui.theme.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    icon: ImageVector,
    iconText: String,
    isExpanded: Boolean,
    onCallAction: () -> Unit,
) {
    ExtendedFloatingActionButton(
        text = {
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
        },
        icon = {
               CustomIcon(
                   iconText = iconText,
                   icon = icon,
                   tint = MaterialTheme.colorScheme.background
               )
        },
        onClick = { onCallAction()},
        containerColor = MaterialTheme.colorScheme.primary,
        expanded = isExpanded
    )
}