package com.example.ifeed.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ifeed.business.LogInViewModel
import com.example.ifeed.business.Provider
import com.example.ifeed.business.SignUpViewModel
import com.example.ifeed.ui.theme.screens.AddPostUi
import com.example.ifeed.ui.theme.screens.FeedUi
import com.example.ifeed.ui.theme.screens.LogInUi
import com.example.ifeed.ui.theme.screens.SignUpUi

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    logInViewModel: LogInViewModel = viewModel(factory = Provider.factory),
    signUpViewModel: SignUpViewModel = viewModel(factory = Provider.factory),
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

        this.composable(route = Locations.SignUp.name) {
            SignUpUi(
                modifier = modifier,
                navController = navController,
                signUpViewModel = signUpViewModel
            ) { alert(it) }
        }

        this.composable(route = Locations.Feed.name) {
            FeedUi(
                modifier = modifier,
                navController = navController
            )
        }

        this.composable(route = Locations.Post.name) {
            AddPostUi(
                modifier = modifier,
                navController = navController
            )
        }
    }
}