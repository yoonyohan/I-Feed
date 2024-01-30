package com.example.ifeed.ui.theme.screens

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ifeed.R
import com.example.ifeed.business.SignUpViewModel
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.components.CustomText
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PasswordSetupUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel,
    alert: (String) -> Unit
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        signUpViewModel.resetPassword()
        navController.popBackStack()
    }

    LaunchedEffect(state.alert) {
        if (state.alert.isNotEmpty()) {
            alert(state.alert)
            signUpViewModel.resetAlert() // Reset flag for next reaction
        }
    }

    LaunchedEffect(state.toLoggedIn) {
        Log.d("LaunchedEffect", "LaunchedEffect triggered with toLoggedIn=${state.toLoggedIn}")
        if (state.toLoggedIn) {
            navController.navigate(route = Locations.LogIn.name)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = dimensionResource(id = R.dimen.screen_horizontal_padding),
                vertical = dimensionResource(id = R.dimen.screen_vertical_padding)
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_10_height)))

        CustomText(
            value = stringResource(R.string.Password_setup_header),
            color = Color.White.copy(alpha = 0.8f),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 30.sp
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        CustomText(
            value = stringResource(R.string.password_setup_description),
            color = Color.White.copy(alpha = 0.8f),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 25.sp
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        PasswordInputFields(
            modifier = modifier,
            navController = navController,
            signUpViewModel = signUpViewModel
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        PasswordContinue(
            modifier = modifier,
            signUpViewModel = signUpViewModel
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PasswordInputFields(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    CustomOutLinedTextField(
        value = state.password,
        placeHolder = stringResource(R.string.password),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = if (state.password.length >= 6) ImeAction.Done else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                coroutineScope.launch(Dispatchers.IO) {
                    if (state.password.length >= 6) {
                        signUpViewModel.createAnAccount()
                    }
                }
            }
        ),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
            focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
            errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(end = dimensionResource(id = R.dimen.row_text_field_padding)),
        onValueChange = {
            signUpViewModel.addPasswordToState(it)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PasswordContinue(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    CustomFilledButton(
        buttonText = stringResource(R.string.sign_up_and_continue),
        modifier = modifier
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            if (state.password.length >= 6) {
                signUpViewModel.createAnAccount()
            }
        }
    }
}