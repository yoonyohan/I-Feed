package com.example.ifeed.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ifeed.ui.theme.components.CustomText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageTopAppBar(
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            CustomText(
                value = "i Messenger",
                modifier = modifier,
                color = Color.White,
                style = TextStyle(
                    fontSize = 15.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        actions = {
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "New")
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
            }
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f)
        )
    )
}