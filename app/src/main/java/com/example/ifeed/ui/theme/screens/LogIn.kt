package com.example.ifeed.ui.theme.screens

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.ifeed.business.LogInViewModel
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LogInUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    logInViewModel: LogInViewModel,
    alert: (String) -> Unit
) {
    val logInState by logInViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val navOptions = NavOptions.Builder()
        .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
        .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
        .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        .build()

    LaunchedEffect(logInState.alert) {
        logInState.alert?.let {
            if (it.isNotEmpty()) {
                alert(it)
            }
        }
    }

    LaunchedEffect(logInState.isLoggedIn) {
        if (logInState.isLoggedIn) {
            navController.navigate(Locations.Feed.name)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.Center
    ){
        Spacer(modifier = Modifier.height(220.dp))

        CustomOutLinedTextField(
            value = logInState.userName,
            placeHolder = "Email or phone number",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (logInState.userName.length > 3) ImeAction.Next else ImeAction.None
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
            coroutineScope.launch {
                logInViewModel.userNameToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = logInState.password,
            placeHolder = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (logInState.password.length > 3) ImeAction.Done else ImeAction.None
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
        ) {
            coroutineScope.launch {
                logInViewModel.passwordToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomFilledButton(
            modifier = modifier,
            buttonText = "Login"
        ) {
            logInViewModel.logIn()
        }

        Spacer(modifier = Modifier.height(220.dp))

        CustomOutLinedButton(
            modifier = modifier,
            buttonText = "Create a new account"
        ) {
            coroutineScope.launch {
                navController.navigate(route = Locations.SignUp.name, navOptions = navOptions)
                delay(500L)
                logInViewModel.resetState()
            }
        }
    }
}