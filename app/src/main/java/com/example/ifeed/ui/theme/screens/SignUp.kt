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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ifeed.business.SignUpState
import com.example.ifeed.business.SignUpViewModel
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel,
    alert: (String) -> Unit
) {
    val signUpState by signUpViewModel.state.collectAsState(initial = SignUpState())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(signUpState.alert) {
        signUpState.alert?.let {
            alert(it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CustomOutLinedTextField(
            value = signUpState.userName,
            placeHolder = "Email or Phone number",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (signUpState.userName.length > 3) ImeAction.Done else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = {}
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            )
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                signUpViewModel.addUserNameToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = signUpState.password,
            placeHolder = "Password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (signUpState.password.length > 3) ImeAction.Done else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = {}
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            )
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                signUpViewModel.addPasswordToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomOutLinedTextField(
            value = signUpState.confirmPassword,
            placeHolder = "Confirm password",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = if (signUpState.confirmPassword.length > 3) ImeAction.Done else ImeAction.None
            ),
            keyboardActions = KeyboardActions(
                onNext = {}
            ),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)
            )
        ) {
            coroutineScope.launch(Dispatchers.IO) {
                signUpViewModel.addConfirmPasswordToState(it)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        CustomFilledButton(buttonText = "Sign Up") {
            coroutineScope.launch(Dispatchers.IO) {
                signUpViewModel.accountCreation()
                delay(1000)
                navController.navigate(route = Locations.LogIn.name)
            }
        }
    }
}