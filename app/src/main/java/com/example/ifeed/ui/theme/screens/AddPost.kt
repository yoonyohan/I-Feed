package com.example.ifeed.ui.theme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.navigation.Locations

@Composable
fun AddPostUi(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.Top
    ) {
        var header: String? by rememberSaveable { mutableStateOf("") }
        var description: String? by rememberSaveable { mutableStateOf("") }
        var topic: String? by rememberSaveable { mutableStateOf("") }

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutLinedTextField(
            value = header ?: "",
            placeHolder = "Good Morning",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (header?.length!! > 3) ImeAction.Next else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = null
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            ),
        ) { header = it }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = description ?: "",
            placeHolder = "So, Today is my birthday...",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (description?.length!! > 3) ImeAction.Next else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = null
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            ),
            singleLine = false,
            modifier = Modifier.height(200.dp)
        ) { description = it }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = topic ?: "",
            placeHolder = "Daily routines",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (topic?.length!! > 3) ImeAction.Next else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = null
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            ),
        ) { topic = it }

        Spacer(modifier = Modifier.height(12.dp))

        CustomFilledButton(buttonText = "Post") {
            navController.navigate(route = Locations.Feed.name)
        }
    }
}