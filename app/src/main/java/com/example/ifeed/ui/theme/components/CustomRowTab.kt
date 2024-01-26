package com.example.ifeed.ui.theme.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ifeed.composables.MessageTopAppBar
import com.example.ifeed.ui.theme.uidata.MessageTabItem
import com.example.ifeed.ui.theme.uidata.tabList

@Composable
fun CustomRowTab(
    modifier: Modifier = Modifier,
    selectedTab: (Int) -> Unit,
    selectedTabIndex: Int ,
    tabItems: List<MessageTabItem> = tabList
) {
    Column {
        MessageTopAppBar()
        TabRow(
            selectedTabIndex =  selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
            contentColor = Color.White.copy(alpha = 0.7f),
            indicator = {
                tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color.White.copy(alpha = 0.7f)
                )
            },
            modifier = modifier
        ) {
            tabItems.forEachIndexed { index, messageTabItem ->
                Tab(
                    selected = index == selectedTabIndex,
                    onClick = {
                              selectedTab(index)
                    },
                    text = {
                        CustomText(
                            value = messageTabItem.tabLabel,
                            color = Color.White.copy(alpha = 0.7f),
                            style = TextStyle(
                                fontSize = 15.sp,
                                letterSpacing = 1.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                )
            }
        }
    }
}