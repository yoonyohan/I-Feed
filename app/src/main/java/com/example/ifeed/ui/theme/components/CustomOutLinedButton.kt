package com.example.ifeed.ui.theme.components

import android.service.autofill.OnClickAction
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomOutLinedButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClickAction: () -> Unit,
) {
    OutlinedButton(
        onClick = { onClickAction() },
        modifier = modifier
            .fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
    ) {
        CustomText(
            value = buttonText,
            color = MaterialTheme.colorScheme.primary.copy(0.8f),
            style = TextStyle(
                fontSize = 15.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}