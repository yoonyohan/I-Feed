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
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.example.ifeed.R
import com.example.ifeed.business.LogInViewModel
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.navigation.Locations

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun LogInUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    logInViewModel: LogInViewModel,
    alert: (String) -> Unit
) {
    val logInState by logInViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(logInState.alert) {
        if (logInState.alert.isNotEmpty()) {
            alert(logInState.alert)
            logInViewModel.resetAlert() // Reset flag for next reaction
        }
    }

    val navOptions = NavOptions.Builder()
        .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
        .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
        .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
        .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        .build()

    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
        focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
        errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
    )

    LaunchedEffect(logInState.isLoggedIn) {
        if(logInState.isLoggedIn) {
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

        LogInInputFields(
            modifier = modifier,
            navController = navController,
            logInViewModel =  logInViewModel,
            textFieldColors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        CustomFilledButton(
            modifier = modifier,
            buttonText = stringResource(R.string.login)
        ) {
            logInViewModel.logIn()
        }

        Spacer(modifier = Modifier.height(220.dp))

        CustomOutLinedButton(
            modifier = modifier,
            buttonText = stringResource(R.string.create_a_new_account)
        ) {
            navController.navigate(route = Locations.NameSetup.name, navOptions = navOptions)
            logInViewModel.resetState()
        }
    }
}

@Composable
fun LogInInputFields(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    logInViewModel: LogInViewModel,
    textFieldColors: TextFieldColors
) {
    val state by logInViewModel.state.collectAsStateWithLifecycle()

    CustomOutLinedTextField(
        modifier = modifier,
        value = state.userName,
        placeHolder = stringResource(R.string.email_or_phone_number),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = if (state.userName.length > 3) ImeAction.Next else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onNext = null
        ),
        colors = textFieldColors,
    ) {
        logInViewModel.userNameToState(it)
    }

    Spacer(modifier = Modifier.height(12.dp))

    CustomOutLinedTextField(
        modifier = modifier,
        value = state.password,
        placeHolder = stringResource(id = R.string.password),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = if (state.password.length > 3) ImeAction.Done else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onNext = {}
        ),
        colors = textFieldColors,
    ) {
        logInViewModel.passwordToState(it)
    }
}