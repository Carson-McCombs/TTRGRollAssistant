package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ToggleableTextField(modifier: Modifier = Modifier, text: String, textColor: Color,  onValueChange: (String) -> Unit){
    val textState = remember { mutableStateOf(text) }
    val editTextState = remember { mutableStateOf(true) }
    BasicTextField(
        modifier = modifier,
        readOnly = editTextState.value,
        enabled = !editTextState.value,
        value = textState.value,
        onValueChange = {
            textState.value = it;
            onValueChange(textState.value)
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Justify,
            color = Color.White
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.wrapContentSize().padding(8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { editTextState.value = !editTextState.value }) {
                    Icon(
                        imageVector = if (editTextState.value) Icons.Default.Edit else Icons.Filled.Edit,
                        contentDescription = "Toggle Editing of Name"
                    )
                }
                innerTextField()
            }

        }
    )
}