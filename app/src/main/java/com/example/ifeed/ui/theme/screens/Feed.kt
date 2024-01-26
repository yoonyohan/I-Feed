package com.example.ifeed.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ifeed.business.FeedViewModel
import com.example.ifeed.ui.theme.components.CustomFloatingActionButton
import com.example.ifeed.ui.theme.components.CustomIcon
import com.example.ifeed.ui.theme.components.CustomText
import com.example.ifeed.ui.theme.navigation.Locations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    feedViewModel: FeedViewModel
) {
    val state by feedViewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()


   Column(
       modifier = modifier
           .fillMaxSize()
   ) {

       Scaffold(
           modifier = modifier
               .fillMaxSize(),
           bottomBar = {
                       BottomAppBar(
                           actions = {
                               IconButton(onClick = { /*TODO*/ }) {
                                   Icon(
                                       imageVector = Icons.Filled.AccountCircle,
                                       contentDescription = "Account"
                                   )
                               }

                               IconButton(onClick = { /*TODO*/ }) {
                                   Icon(
                                       imageVector = Icons.Filled.Notifications,
                                       contentDescription = "Notifications"
                                   )
                               }

                               IconButton(
                                   onClick = {
                                       coroutineScope.launch {
                                           navController.navigate(route = Locations.Messages.name)
                                       }
                                   }
                               ) {
                                   Icon(
                                       imageVector = Icons.Filled.Email,
                                       contentDescription = "Messages"
                                   )
                               }

                               IconButton(
                                   onClick = {
                                       coroutineScope.launch {
                                           feedViewModel.readAllPosts()
                                       }
                                   }
                               ) {
                                   Icon(
                                       imageVector = Icons.Filled.Refresh,
                                       contentDescription = "Refresh"
                                   )
                               }
                           },
                           floatingActionButton = {
                               CustomFloatingActionButton(
                                   icon = Icons.Filled.Edit,
                                   iconText = "New Post",
                               ) {
                                   navController.navigate(route = Locations.Post.name)
                               }
                           },
                           containerColor = Color.Black,
                           contentColor = Color.White
                       )
           },
           containerColor = Color.Black
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
                           .padding(vertical = 4.dp),
                       colors = CardDefaults.cardColors(
                           containerColor = MaterialTheme.colorScheme.background
                       )
                   ) {
                       Column(
                           modifier = modifier
                               .fillMaxWidth()
                               .fillMaxHeight()
                               .padding(horizontal = 20.dp, vertical = 20.dp),
                           verticalArrangement = Arrangement.SpaceEvenly
                       ) {
                           Row(
                               modifier = modifier.fillMaxWidth(1f),
                               horizontalArrangement = Arrangement.SpaceBetween
                           ) {
                               CustomText(
                                   value = it.name,
                                   modifier = modifier,
                                   color = Color.White,
                                   style = TextStyle(
                                       fontSize = 15.sp,
                                       letterSpacing = 1.sp,
                                       fontWeight = FontWeight.Medium
                                   )
                               )
                           }
                           Spacer(modifier = Modifier.height(20.dp))
                           CustomText(
                               value = it.content,
                               modifier = modifier,
                               color = Color.White,
                               style = TextStyle(
                                   fontSize = 15.sp,
                                   letterSpacing = 1.sp,
                                   fontWeight = FontWeight.Light,
                                   lineHeight = 23.sp
                               )
                           )
                           Spacer(modifier = Modifier.height(20.dp))

                           Row(
                               modifier = modifier.fillMaxWidth(1f),
                               horizontalArrangement = Arrangement.End,
                               verticalAlignment = Alignment.CenterVertically
                           ) {
                               IconButton(onClick = { /*TODO*/ }) {
                                   CustomIcon(
                                       iconText = "Like",
                                       icon = Icons.Filled.FavoriteBorder,
                                       tint = Color.White.copy(alpha = 0.8f),
                                       modifier = modifier.size(20.dp)
                                   )
                               }

                               Row {
                                   IconButton(onClick = { /*TODO*/ }) {
                                       CustomIcon(
                                           iconText = "Share",
                                           icon = Icons.Filled.Send,
                                           tint = Color.White.copy(alpha = 0.8f),
                                           modifier = modifier.size(20.dp)
                                       )
                                   }
                                   IconButton(onClick = { /*TODO*/ }) {
                                       CustomIcon(
                                           iconText = "More",
                                           icon = Icons.Filled.MoreVert,
                                           tint = Color.White.copy(alpha = 0.8f),
                                           modifier = modifier.size(20.dp)
                                       )
                                   }
                               }
                           }
                       }
                   }
               }
           }
       }
   }
}