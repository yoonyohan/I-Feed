package com.example.ifeed.ui.theme.uidata

data class MessageTabItem(
    val tabLabel: String
)

val tabList = listOf<MessageTabItem>(
    MessageTabItem("Featured"),
    MessageTabItem("Active"),
    MessageTabItem("New")
)
