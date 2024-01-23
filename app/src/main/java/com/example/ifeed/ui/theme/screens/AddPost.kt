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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ifeed.business.AddNewPostViewModel
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddPostUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    addNewPostViewModel: AddNewPostViewModel,
    alert: (String) -> Unit
) {
    val state by addNewPostViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.alert) {
        state.alert?.let {
            if (it.isNotEmpty()) {
                alert(it)
            }
        }
    }

    DisposableEffect(state.postCreationSuccess) {
        if (state.postCreationSuccess) {
            navController.navigate(route = Locations.Feed.name)
        }
        onDispose {  }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        CustomOutLinedTextField(
            value = state.title,
            placeHolder = "Good Morning",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (state.title.length > 3) ImeAction.Next else ImeAction.None
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
        ) { coroutineScope.launch(Dispatchers.IO) {
            addNewPostViewModel.addTitleToState(it)
        } }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = state.description,
            placeHolder = "So, Today is my birthday...",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (state.description.length > 3) ImeAction.Next else ImeAction.None
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
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                addNewPostViewModel.addDescriptionToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = state.topic,
            placeHolder = "Daily routines",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (state.topic.length > 3) ImeAction.Next else ImeAction.None
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
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                addNewPostViewModel.addTopicToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomFilledButton(buttonText = "Post") {
            coroutineScope.launch(Dispatchers.IO) {
                addNewPostViewModel.addNewPost()
            }
        }
    }
}