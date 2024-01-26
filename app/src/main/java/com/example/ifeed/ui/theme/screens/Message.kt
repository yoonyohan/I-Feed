package com.example.ifeed.ui.theme.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ifeed.ui.theme.components.CustomRowTab
import com.example.ifeed.ui.theme.uidata.tabList

@Composable
fun MessageUi(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    alert: (String) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = Color.Black,
        topBar = {
            Spacer(modifier = Modifier.height(20.dp))
            CustomRowTab(
                modifier = modifier,
                selectedTab = {selectedTab = it},
                selectedTabIndex = selectedTab,
                tabItems = tabList
            )
        }
    ) {
        paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = modifier
        ) {

        }
    }
}