package com.example.ifeed.ui.theme.screens

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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ifeed.business.FeedViewModel
import com.example.ifeed.data.FeedPost
import com.example.ifeed.ui.theme.components.CustomFloatingActionButton
import com.example.ifeed.ui.theme.components.CustomIcon
import com.example.ifeed.ui.theme.components.CustomText
import com.example.ifeed.ui.theme.navigation.Locations

@Composable
fun FeedUi(modifier: Modifier = Modifier, navController: NavHostController, feedViewModel: FeedViewModel) {
    val state by feedViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.signOut, block = {
        if (state.signOut) {
            navController.navigate(Locations.LogIn.name)
        }
    })

   Column(modifier = modifier.fillMaxSize()) {
       Scaffold(
           modifier = modifier.fillMaxSize(),
           bottomBar = { BottomAppBar(modifier = modifier, navController = navController, feedViewModel = feedViewModel) },
           containerColor = Color.Black
       ) {
           paddingValues ->
           LazyColumn(contentPadding = paddingValues, modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
               items(state.postFireList) { feedPost ->
                   PostCard(feedPost, feedViewModel)
               }
           }
       }
   }
}
@Composable
fun BottomAppBar(modifier: Modifier = Modifier, navController: NavHostController, feedViewModel: FeedViewModel) {
    BottomAppBar(
        actions = {
            AccountIcon(navController)
            NotificationIcon(navController)
            MessagesIcon(navController)
            RefreshIcon(feedViewModel,navController)
        },
        floatingActionButton = { FabIcon(navController) },
        containerColor = Color.Black,
        contentColor = Color.White,
        modifier = modifier
    )
}
@Composable
fun FabIcon(navController: NavHostController) {
    CustomFloatingActionButton(
        icon = Icons.Filled.Edit,
        iconText = "New Post",
    ) { navController.navigate(route = Locations.Post.name) }
}
@Composable
fun AccountIcon(navController: NavHostController) {
    IconButton(onClick = { navController.navigate(Locations.Account.name) }) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Account"
        )
    }
}
@Composable
fun NotificationIcon(navController: NavHostController) {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = "Notifications"
        )
    }
}
@Composable
fun MessagesIcon(navController: NavHostController) {
    IconButton(
        onClick = {
            navController.navigate(route = Locations.Messages.name)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = "Messages"
        )
    }
}
@Composable
fun RefreshIcon(feedViewModel: FeedViewModel, navController: NavHostController) {
    IconButton(
        onClick = {
            feedViewModel.signOut()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = "Refresh"
        )
    }
}
@Composable
fun PostCard(feedPost: FeedPost, feedViewModel: FeedViewModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors( containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(modifier = modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
                CustomText(
                    value = feedPost.name,
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
                value = feedPost.content,
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
                CardLike(feedViewModel, modifier)
                Row { CardShare(feedViewModel, modifier) ; CardMore(feedViewModel, modifier) }
            }
        }
    }
}
@Composable
fun CardLike(feedViewModel: FeedViewModel, modifier: Modifier = Modifier) {
    IconButton(onClick = { /*TODO*/ }) {
        CustomIcon(
            iconText = "Like",
            icon = Icons.Filled.FavoriteBorder,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = modifier.size(20.dp)
        )
    }
}
@Composable
fun CardShare(feedViewModel: FeedViewModel, modifier: Modifier) {
    IconButton(onClick = { /*TODO*/ }) {
        CustomIcon(
            iconText = "Share",
            icon = Icons.Filled.Send,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = modifier.size(20.dp)
        )
    }
}
@Composable
fun CardMore(feedViewModel: FeedViewModel, modifier: Modifier) {
    IconButton(onClick = { /*TODO*/ }) {
        CustomIcon(
            iconText = "More",
            icon = Icons.Filled.MoreVert,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = modifier.size(20.dp)
        )
    }
}