package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.carsonmccombs.skillviewerfourcompose.stat.Stat
import com.carsonmccombs.skillviewerfourcompose.stat.StatEvent
import com.carsonmccombs.skillviewerfourcompose.stat.StatViewModel
import com.carsonmccombs.skillviewerfourcompose.statmodifier.StatModifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStatModifierDialog(viewmodel: StatViewModel, stat: Stat, dismissDialog: () -> Unit) {

    //nd stands for non-dependent
    //val ndStatIDs = viewmodel.statNonDependencies.collectAsState().value.statNonDependencies[stat.id]?: emptyList()
    //val ndStatIDs = viewmodel.unrelatedStats.collectAsState(emptyMap()).value[stat.id]?: emptyList()
    val ndStatIDs = viewmodel.unrelatedStats.collectAsState(emptyMap()).value[stat.id]?: emptyList()
    val isStatDependent = remember { mutableStateOf(false) }
    val ndStatNames= viewmodel.getStatNames(ndStatIDs).collectAsState(
        initial = emptyMap<Int,String>()
    )
    val statTotals: State<Map<Int, Int>> = viewmodel.statTotals.collectAsState(initial = emptyMap())
    //val ndStatTotals = viewmodel.getTotalStatModifierValues(ndStatIDs?: emptyList()).collectAsState(initial = emptyMap())

    val nameState = remember { mutableStateOf("") }
    val typeState = remember { mutableStateOf("") }
    val valueState = remember { mutableStateOf(0) }
    val childIDState: MutableState<Int?> = remember{ mutableStateOf(null) }

    AlertDialog(modifier = Modifier
        .size(400.dp), onDismissRequest = { dismissDialog() }) {
        Card(){
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = Color.White,
                contentColor = Color.Blue,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        viewmodel.onEvent(
                            StatEvent.UpsertStatModifier(
                                StatModifier(
                                    statID = stat.id,
                                    name = nameState.value,
                                    type = typeState.value,
                                    value = valueState.value,
                                    childStatID = childIDState.value
                                )
                            )

                        )
                        dismissDialog()
                    }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Adds Modifier")
                    }
                }
            ) { padding ->
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(padding),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(modifier = Modifier.weight(1f), text = "Add Modifier", textAlign = TextAlign.Center, style = MaterialTheme.typography.titleLarge)
                        IconButton(onClick = { dismissDialog() }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Closes Modifier Dialog")
                        }
                    }
                    TextField(value = nameState.value, onValueChange = {nameState.value = it}, label = { Text("Name") }, readOnly = isStatDependent.value)
                    TextField(value = typeState.value, onValueChange = {typeState.value = it}, label = { Text("Type") }, readOnly = isStatDependent.value)
                    TextField(value = "${valueState.value}", onValueChange = {
                        if (it.isEmpty()) {
                            valueState.value = 0
                        }
                        else {
                            valueState.value = if (it.isDigitsOnly()) Integer.parseInt(it) else { valueState.value }
                        }
                    }, label = { Text("Value") }, readOnly = isStatDependent.value)
                    Row(
                        modifier = Modifier.wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Is Stat Dependent")
                        Checkbox(checked = isStatDependent.value, onCheckedChange = {
                            isStatDependent.value = it
                            if (!it){
                                childIDState.value = null
                                nameState.value = ""
                                typeState.value = ""
                                valueState.value = 0
                            }
                        })
                    }
                    //Log.d("Twab", "LOADED STAT ${ndStatIDs.toString()}")
                    AnimatedVisibility(visible = isStatDependent.value) {
                        StatDropDownMenu(
                            statIDs = ndStatIDs?: emptyList(),
                            statNames = ndStatNames.value,
                            statTotals = statTotals.value
                            )
                        { statID ->
                            nameState.value = (ndStatNames.value[statID]?: "EMPTY STAT NAME") as String
                            typeState.value = "Stat"
                            valueState.value = 0//ndStatTotals.value[statID]?: -1
                            childIDState.value = statID
                        }
                    }

                }

            }
        }

    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatDropDownMenu(statIDs: List<Int>, statNames: Map<Int, String>, statTotals: Map<Int,Int>, onSelect: (Int) -> Unit){
    val statDependencyExpanded = remember { mutableStateOf(false) }
    val currentlySelectedDependency = remember { mutableStateOf(-1) }
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(expanded = statDependencyExpanded.value, onExpandedChange = { statDependencyExpanded.value = it}) {
            TextField(modifier = Modifier.menuAnchor(), value = statNames[currentlySelectedDependency.value]?: "Empty", onValueChange = {}, readOnly = true)
            DropdownMenu(expanded = statDependencyExpanded.value, onDismissRequest = { statDependencyExpanded.value = false }) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    statIDs.forEach { id ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${statNames[id]?: "Empty Stat Name"} ( ${statTotals[id]?:0} )"
                                )
                            },
                            onClick = {
                                currentlySelectedDependency.value = id
                                statDependencyExpanded.value = false
                                onSelect(id)
                            }
                        )
                    }
                }
            }
        }
    }

}

