package com.example.ifeed.ui.theme.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ifeed.business.AccountViewModel
import com.example.ifeed.ui.theme.components.CustomText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserAccountUi(modifier: Modifier = Modifier, accountViewModel: AccountViewModel) {

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { AccountAppBar(modifier)}
    ) {paddingValues ->
        LazyColumn(
            contentPadding = paddingValues
        ) {
            item {
                ImageAndCover(
                    modifier = modifier,
                    accountViewModel = accountViewModel
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            CustomText(
                value = "I Feed",
                color = Color.White,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 3.sp,
                    fontSize = 20.sp
                ),
                modifier = modifier
            )
        },

        modifier = modifier
    )
}
@Composable
fun ImageAndCover(modifier: Modifier = Modifier, accountViewModel: AccountViewModel) {

    val state by accountViewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        if (uri != null) {
            coroutineScope.launch(Dispatchers.IO) {
                accountViewModel.addProfileImage(uri)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)),
    ) {

        Box(
            modifier = modifier
                .background(
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                    CircleShape
                )
                .size(110.dp)
                .align(Alignment.Center)
                .clickable {
                    launcher.launch("image/*")
                }
        ) {
            AsyncImage(
                model = state.profileImageUrl,
                contentDescription = "ProfileImage",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(110.dp)
                    .clip(CircleShape)
            )
        }
    }
}