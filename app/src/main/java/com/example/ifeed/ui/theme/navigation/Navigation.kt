package com.example.ifeed.ui.theme.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ifeed.business.AddNewPostViewModel
import com.example.ifeed.business.FeedViewModel
import com.example.ifeed.business.LogInViewModel
import com.example.ifeed.business.Provider
import com.example.ifeed.business.SignUpViewModel
import com.example.ifeed.ui.theme.screens.AddPostUi
import com.example.ifeed.ui.theme.screens.EmailOrNumberUi
import com.example.ifeed.ui.theme.screens.FeedUi
import com.example.ifeed.ui.theme.screens.LogInUi
import com.example.ifeed.ui.theme.screens.MessageUi
import com.example.ifeed.ui.theme.screens.NameSetupUi
import com.example.ifeed.ui.theme.screens.PasswordSetupUi

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    logInViewModel: LogInViewModel = viewModel(factory = Provider.factory),
    signUpViewModel: SignUpViewModel = viewModel(factory = Provider.factory),
    addNewPostViewModel: AddNewPostViewModel = viewModel(factory = Provider.factory),
    feedViewModel: FeedViewModel = viewModel(factory = Provider.factory),
    alert: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = Locations.LogIn.name) {
        this.composable(route = Locations.LogIn.name) {
            LogInUi(
                modifier = modifier,
                navController = navController,
                logInViewModel = logInViewModel,
                alert = {alert(it)}
            )
        }

        this.composable(route = Locations.NameSetup.name) {
            NameSetupUi(
                modifier = modifier,
                navController = navController,
                signUpViewModel = signUpViewModel
            )
        }

        this.composable(route = Locations.PasswordSetup.name) {
            PasswordSetupUi(
                modifier = modifier,
                navController = navController,
                signUpViewModel = signUpViewModel
            ) { alert(it) }
        }

        this.composable(route = Locations.EmailOrNumber.name) {
            EmailOrNumberUi(
                modifier = modifier,
                navController = navController,
                signUpViewModel = signUpViewModel
            )
        }

        this.composable(route = Locations.Feed.name) {
            FeedUi(
                modifier = modifier,
                feedViewModel = feedViewModel,
                navController = navController
            )
        }

        this.composable(route = Locations.Post.name) {
            AddPostUi(
                modifier = modifier,
                addNewPostViewModel = addNewPostViewModel,
                navController = navController
            ) { alert(it) }
        }

        this.composable(route = Locations.Messages.name) {
            MessageUi(
                modifier = modifier,
                navController = navController
            ) {}
        }
    }
}