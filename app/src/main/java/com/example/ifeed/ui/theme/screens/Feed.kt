package com.example.ifeed.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.ifeed.ui.theme.components.CustomFloatingActionButton
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.delay

@Composable
fun FeedUi(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

   Column(
       modifier = modifier
           .fillMaxSize()
   ) {

       LaunchedEffect(Unit) {
           while (true) {
               delay(5000)
               isExpanded = true
               delay(10000)
               isExpanded = false
           }
       }
       Scaffold(
           modifier = modifier
               .fillMaxSize(),
           floatingActionButton = {
               CustomFloatingActionButton(
                   buttonText = "Compose",
                   icon = Icons.Filled.Edit,
                   iconText = "New Post",
                   isExpanded = isExpanded
               ) {
                   navController.navigate(route = Locations.Post.name)
               }
           },
           floatingActionButtonPosition = FabPosition.End
       ) {
           paddingValues ->
           LazyColumn(
               contentPadding = paddingValues
           ) {

           }
       }
   }
}