@file:OptIn(ExperimentalMaterial3Api::class)

package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.carsonmccombs.skillviewerfourcompose.stat.StatEvent
import com.carsonmccombs.skillviewerfourcompose.stat.StatViewModel
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier

@Composable
fun StatModifierCard(id: Int, viewmodel: StatViewModel){
    val statModifier: State<StatModifier> = viewmodel.getStatModifier(id).collectAsState(initial = StatModifier())


    val isStatDependent = statModifier.value.childStatID != null
    if (isStatDependent && statModifier.value.childStatID == null) return
    val nameMap = viewmodel.statNames.collectAsState()
    val statName = if (isStatDependent) nameMap.value[statModifier.value.childStatID] else null

    val totalMap = viewmodel.statTotals.collectAsState()
    val statTotal = if (isStatDependent) totalMap.value[statModifier.value.childStatID] else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        //.background(MaterialTheme.colorScheme.primary),

        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),

            ) {
            Spacer(modifier = Modifier.width(8.dp))


            StatModifier_TextField_StringInput(
                //modifier = Modifier.weight(1f),
                id = statModifier.value.id,
                modifier = Modifier,
                text = if (isStatDependent) statName?:"N" else statModifier.value.name,
                onValueChangeEvent = {
                    viewmodel.onEvent(
                        StatEvent.UpsertStatModifier(
                            statModifier = statModifier.value.copy(
                                name = it
                            )
                        )
                    )
                },
                labelText = "Source",
                readOnly = isStatDependent
            )
            StatModifier_TextField_StringInput(
                //modifier = Modifier.weight(1f),
                id = statModifier.value.id,
                modifier = Modifier,
                text = statModifier.value.type,
                onValueChangeEvent = {
                    viewmodel.onEvent(
                        StatEvent.UpsertStatModifier(
                            statModifier = statModifier.value.copy(
                                type = it
                            )
                        )
                    )
                },
                labelText = "Type",
                readOnly = isStatDependent
            )
            //Log.d("Twab", "STAT ($id) VALUE: ${statModifier.value.toString()} -> ${statValue.value} ")
            StatModifier_TextField_IntegerInput(
                //modifier = Modifier.weight(1f),
                id = statModifier.value.id,
                modifier = Modifier,
                text = if (isStatDependent) statTotal?.toString()?:"0" else statModifier.value.value.toString(),
                onValueChangeEvent = {
                    viewmodel.onEvent(
                        StatEvent.UpsertStatModifier(
                            statModifier = statModifier.value.copy(
                                value = it
                            )
                        )
                    )
                },
                labelText = "Value",
                readOnly = isStatDependent
            )

            IconButton(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxHeight(),
                onClick = { viewmodel.onEvent(StatEvent.DeleteStatModifier(statModifier.value)) }
            ) {
                Icon(
                    //modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Deletes this Modifier"
                )
            }

        }

    }

}

@Composable
private fun StatModifier_TextField_StringInput(modifier: Modifier, text: String, id: Int, labelText: String, readOnly: Boolean, onValueChangeEvent: (String) -> Unit){
    val state = remember(id,labelText) { mutableStateOf(text) }
    val maxLength = 10
    //val borderColor = MaterialTheme.colorScheme.errorContainer// if (state.value.isEmpty()) { MaterialTheme.colorScheme.errorContainer } else { Color.Transparent }
    BasicTextField(
        value = state.value,
        onValueChange = {newText ->
            if (newText.length <= maxLength){
                state.value = newText.trimEnd('\n')
                onValueChangeEvent(state.value)
            }

        },
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = 8.dp)
                    .padding(4.dp),
            ){
                if (state.value.isEmpty()){
                    Text(
                        text = labelText,
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                    )
                }
                innerTextField()
            }
        }
        )



}

@Composable
private fun StatModifier_TextField_IntegerInput(modifier: Modifier, text: String, id: Int, labelText: String, readOnly: Boolean, onValueChangeEvent: (Int) -> Unit){
    val state = remember(id,labelText) { mutableStateOf(text) }
    val maxLength = 10
    //val borderColor = MaterialTheme.colorScheme.errorContainer// if (state.value.isEmpty()) { MaterialTheme.colorScheme.errorContainer } else { Color.Transparent }
    BasicTextField(
        value = state.value,
        onValueChange = {newText ->
            if (newText.length <= maxLength){
                state.value = newText.replace(Regex("/^\\d+\$/"), "")
                if (state.value.isBlank()) { state.value = "0" }
                onValueChangeEvent(Integer.parseInt(state.value))
            }
        },
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = modifier
                    .wrapContentSize()
                    .padding(start = 8.dp)
                    .padding(4.dp),
            ){
                if (state.value.isEmpty()){
                    Text(
                        text = labelText,
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.LightGray)
                    )
                }
                innerTextField()
            }
        }
    )


}