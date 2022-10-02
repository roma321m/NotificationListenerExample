package com.roman.notificationlistenerexample.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun MainScreen(
    text: String,
    onClickCreateNotify: () -> Unit,
    onClickClearAll: () -> Unit,
    onClickList: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            onClick = onClickCreateNotify
        ) {
            Text(
                text = "Create Notify",
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            onClick = onClickClearAll
        ) {
            Text(
                text = "Clear All",
                fontWeight = FontWeight.Bold
            )
        }
        Button(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            onClick = onClickList
        ) {
            Text(
                text = "List",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.h6
        )
    }
}