package com.example.ifeed.ui.theme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ifeed.ui.theme.IFeedTheme

@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    onDismissRequest: () -> Unit
) {
    Box(
        modifier = modifier
            .height(20.dp)
            .wrapContentSize(align = Alignment.TopCenter)
    ) {
        DropdownMenu(
            expanded = expanded,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(horizontal = 10.dp),
            onDismissRequest = { onDismissRequest()}
        ) {
            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item One",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item Two",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item One",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item One",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item One",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item One",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item Two",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item Two",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )

            DropdownMenuItem(
                text = {
                    CustomText(
                        value = "Item Two",
                        color = MaterialTheme.colorScheme.primary.copy(0.8f),
                        style = TextStyle(
                            fontSize = 15.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                onClick = { /*TODO*/ }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    IFeedTheme {
        CustomDropDownMenu {}
    }
}