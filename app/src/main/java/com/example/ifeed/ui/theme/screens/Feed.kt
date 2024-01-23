package com.example.ifeed.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ifeed.business.FeedViewModel
import com.example.ifeed.ui.theme.components.CustomFloatingActionButton
import com.example.ifeed.ui.theme.components.CustomText
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    feedViewModel: FeedViewModel
) {
    var isExpanded by rememberSaveable { mutableStateOf(true) }
    val state by feedViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()


   Column(
       modifier = modifier
           .fillMaxSize()
   ) {

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
                   coroutineScope.launch {
                       isExpanded = false
                       delay(500L)
                       navController.navigate(route = Locations.Post.name)
                   }
               }
           },
           floatingActionButtonPosition = FabPosition.End
       ) {
           paddingValues ->
           LazyColumn(
               contentPadding = paddingValues,
               modifier = modifier
                   .padding(horizontal = 8.dp, vertical = 4.dp)
           ) {
                items(state.postFireList) {
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        ) {
                            CustomText(
                                value = it.userEmail,
                                modifier = modifier,
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(13.dp))

                            CustomText(
                                value = it.title,
                                modifier = modifier,
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 22.sp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            CustomText(
                                value = it.description,
                                modifier = modifier,
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            CustomText(
                                value = it.topic,
                                modifier = modifier,
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    letterSpacing = 1.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
           }
       }
   }
}