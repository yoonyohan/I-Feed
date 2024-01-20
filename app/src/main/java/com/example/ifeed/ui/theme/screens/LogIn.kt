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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.navigation.Locations

@Composable
fun LogInUi(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var userName: String? by rememberSaveable { mutableStateOf("") }
    var password: String? by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(220.dp))

        CustomOutLinedTextField(
            value = userName ?: "",
            placeHolder = "Email or phone number",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (userName?.length!! > 3) ImeAction.Next else ImeAction.None
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
        ) { userName = it }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = password ?: "",
            placeHolder = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (password?.length!! > 3) ImeAction.Done else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    navController.navigate(route = Locations.Feed.name)
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            ),
        ) { password = it }

        Spacer(modifier = Modifier.height(12.dp))

        CustomFilledButton(
            modifier = modifier,
            buttonText = "Login"
        ) {
            navController.navigate(route = Locations.Feed.name)
        }

        Spacer(modifier = Modifier.height(220.dp))

        CustomOutLinedButton(
            modifier = modifier,
            buttonText = "Create a new account"
        ) {
            navController.navigate(route = Locations.SignUp.name)
            userName = ""
            password = ""
        }
    }
}