package com.example.ifeed.ui.theme.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ifeed.BackgroundTask
import com.example.ifeed.R
import com.example.ifeed.business.SignUpViewModel
import com.example.ifeed.ui.theme.components.CustomFilledButton
import com.example.ifeed.ui.theme.components.CustomOutLinedTextField
import com.example.ifeed.ui.theme.components.CustomText
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun NameSetupUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    BackHandler(enabled = true) {
        navController.popBackStack()
        signUpViewModel.resetNames()
    }

    val textFiledColors = TextFieldDefaults.colors(
        focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
        focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
        errorIndicatorColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    )

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
            value = stringResource(R.string.enter_your_real_name),
            color = Color.White.copy(alpha = 0.8f),
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 30.sp
            )
        )

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        CustomText(
            value = stringResource(R.string.name_setup_description),
            color = Color.White.copy(alpha = 0.8f),
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.W400,
                lineHeight = 25.sp
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        Row(
            modifier = modifier
                .fillMaxWidth(1f)
        ) {
            NameInputFields(
                modifier = modifier,
                textFiledColors = textFiledColors,
                navController = navController,
                signUpViewModel = signUpViewModel
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.spacer_20_height)))

        NameSetupContinue(
            modifier = modifier,
            navController = navController,
            signUpViewModel = signUpViewModel
        )
    }
}


@Composable
fun NameInputFields(
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel,
    textFiledColors: TextFieldColors,
    navController: NavHostController
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()

    CustomOutLinedTextField(
        value = state.firstName,
        placeHolder = stringResource(R.string.first_name),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = if (state.firstName.length >= 2) ImeAction.Next else ImeAction.None
        ),
        keyboardActions = KeyboardActions(),
        colors =  textFiledColors,
        modifier = modifier
            .fillMaxWidth(0.5f)
            .padding(end = dimensionResource(id = R.dimen.row_text_field_padding)),
        onValueChange = {
            signUpViewModel.addFirstNameToState(it)
        }
    )

    CustomOutLinedTextField(
        value = state.lastName,
        placeHolder = stringResource(R.string.last_name),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
            imeAction = if (state.lastName.length >= 2) ImeAction.Done else ImeAction.None
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (state.firstName.length >= 2 && state.lastName.length >= 2) {
                    navController.navigate(Locations.EmailOrNumber.name)
                }
            }
        ),
        colors = textFiledColors,
        modifier = modifier
            .padding(start = dimensionResource(id = R.dimen.row_text_field_padding)),
        onValueChange = {
            signUpViewModel.addLastNameToState(it)
        }
    )
}

@Composable
fun NameSetupContinue(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signUpViewModel: SignUpViewModel
) {
    val state by signUpViewModel.state.collectAsStateWithLifecycle()

    CustomFilledButton(
        buttonText = stringResource(R.string.continue_buuton),
        modifier = modifier
    ) {
        if (state.firstName.length >= 2 && state.lastName.length >= 2) {
            navController.navigate(Locations.EmailOrNumber.name)
        }
    }
}



@OptIn(DelicateCoroutinesApi::class)
fun performBackgroundTaskForNameSetup(navController: NavController) {

    val backgroundTask = BackgroundTask()

    val callBack = object : BackgroundTask.TaskCallBack {
        override fun onResult(result: String) {
            GlobalScope.launch(Dispatchers.Main) {
                if (navController.currentBackStackEntry != null) {
                    navController.navigate(Locations.EmailOrNumber.name)
                }
            }
        }
    }
    
    backgroundTask.performBackgroundTask(callBack)
}