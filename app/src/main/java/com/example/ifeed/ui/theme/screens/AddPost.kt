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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ifeed.R
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

        AddPostInputFields(
            modifier = modifier,
            addPostViewModel = addNewPostViewModel
        )

        Spacer(modifier = Modifier.height(12.dp))

        PostButton(
            modifier = modifier,
            addNewPostViewModel = addNewPostViewModel
        )
    }
}

@Composable
fun AddPostInputFields(
    modifier: Modifier = Modifier,
    addPostViewModel: AddNewPostViewModel
) {
    val state by addPostViewModel.state.collectAsState()

    CustomOutLinedTextField(
        value = state.content,
        placeHolder = stringResource(R.string.add_post_placeholder),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = if (state.content.length > 3) ImeAction.Done else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onDone = {}
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
            errorIndicatorColor = Color.Transparent,
        ),
        singleLine = false,
        modifier = modifier.height(300.dp)
    ) {
        addPostViewModel.addContentToState(it)
    }
}

@Composable
fun PostButton(
    modifier: Modifier = Modifier,
    addNewPostViewModel: AddNewPostViewModel,
) {
    CustomFilledButton(buttonText = "Post", modifier = modifier) {
        addNewPostViewModel.writePost()
    }
}