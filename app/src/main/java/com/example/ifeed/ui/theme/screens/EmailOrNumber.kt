package com.example.ifeed.ui.theme.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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

@Composable
fun EmailOrNumberUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    BackHandler(enabled = true) {
        navController.popBackStack()
        signUpViewModel.resetContact()
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
            value = stringResource(R.string.email_or_password),
            color = Color.White.copy(alpha = 0.8f),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 30.sp
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        CustomText(
            value = stringResource(R.string.Contact_Setup_header),
            color = Color.White.copy(alpha = 0.8f),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 25.sp
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        ContactInputField(
            modifier = modifier,
            navController = navController,
            signUpViewModel = signUpViewModel
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        ContactContinue(
            navController = navController,
            signUpViewModel = signUpViewModel
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        Column(
            modifier= modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ContactChosen(
                modifier = modifier,
                signUpViewModel = signUpViewModel
            )
        }
    }
}

@Composable
fun ContactInputField(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()

    CustomOutLinedTextField(
        value = if(state.emailAddressOn) {
            state.email
        } else if (state.phoneNumberOn) {
            state.phoneNumber
        }else {""},
        placeHolder = if (state.emailAddressOn) {
            stringResource(R.string.phone_number)
        } else {
            stringResource(R.string.email_address)
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = if (state.email.length >= 2 || state.phoneNumber.length >= 8) ImeAction.Done else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (state.email.length >= 2 || state.phoneNumber.length >= 8) {
                    navController.navigate(route = Locations.PasswordSetup.name)
                }
            }
        ),
        colors =  TextFieldDefaults.colors(
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
            if (state.emailAddressOn) {
                signUpViewModel.addEmailOrPhoneNumberToState(it,"email")
            } else if (state.phoneNumberOn) {
                signUpViewModel.addEmailOrPhoneNumberToState(it,"phone")
            }
        }
    )
}

@Composable
fun ContactContinue(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()

    CustomFilledButton(
        buttonText = "Continue",
        modifier = modifier
    ) {
        if (state.email.length >= 2 || state.phoneNumber.length >= 8) {
            navController.navigate(route = Locations.PasswordSetup.name)
        }
    }
}

@Composable
fun ContactChosen(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()

    if (state.phoneNumberOn) {
        CustomText(
            value = stringResource(R.string.use_phone_number),
            color = Color.White.copy(alpha = 0.5f),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 30.sp
            ),
            modifier = modifier
                .clickable {
                    signUpViewModel.onEmail(true)
                    signUpViewModel.onPhoneNumberOn(false)
                }
        )
    }

    if (state.emailAddressOn) {
        CustomText(
            value = stringResource(R.string.use_email_address),
            color = Color.White.copy(alpha = 0.5f),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 30.sp
            ),
            modifier = modifier
                .clickable {
                    signUpViewModel.onPhoneNumberOn(true)
                    signUpViewModel.onEmail(false)
                }
        )
    }
}