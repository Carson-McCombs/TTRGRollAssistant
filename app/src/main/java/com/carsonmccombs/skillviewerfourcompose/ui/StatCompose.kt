package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carsonmccombs.skillviewerfourcompose.stat.Stat
import com.carsonmccombs.skillviewerfourcompose.stat.StatViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsCompose(stat: Stat, viewmodel: StatViewModel){

    val openModifierDialog = remember(stat.id, "openModifierDialog") { mutableStateOf(false) }

    val statModifierIDs = viewmodel.getStatModifierIDs(stat.id).collectAsState(initial = emptyList())

    Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            //topBar = { ModifierContainerTitle(stat, statTotal = statTotal, viewmodel::onEvent, swapToSettings) },
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    FloatingActionButton(
                        modifier = Modifier
                            .size(48.dp),
                        onClick = {
                            openModifierDialog.value = true
                            //viewmodel.onEvent(StatEvent.UpsertStatModifier(StatModifier(statID = stat.id, childStatID = if (stat.id == 0) -1 else 0)))
                        },
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 16.dp,
                            pressedElevation = 4.dp,
                            focusedElevation = 12.dp,
                            hoveredElevation = 24.dp
                        ),
                        shape = CircleShape,
                        content = {

                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Opens Modifier Creation Dialog",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                            )


                        }
                    )

                }

            },
        ){ padding ->
            AnimatedVisibility(visible = openModifierDialog.value) {
                CreateStatModifierDialog(viewmodel = viewmodel, stat = stat) {
                    openModifierDialog.value = false
                }
            }
            ModifierContainer(statModifierIDs = statModifierIDs.value, viewmodel = viewmodel, paddingValues = padding)


//        }



        }




}

@Composable
fun ModifierContainer(statModifierIDs: List<Int>, viewmodel: StatViewModel, paddingValues: PaddingValues){

    Column{

        LazyColumn(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(items = statModifierIDs, itemContent = { id -> StatModifierCard(id = id, viewmodel = viewmodel) })

        }
    }
}
/*
@Composable
fun ModifierContainerTitle(stat: Stat, statTotal: Int, onEvent: (StatEvent) -> Unit, swapToSettings: () -> Unit){
    //val nameState = remember { mutableStateOf(stat.name) }
    val name = stat.name
    Card(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        //onClick = { editNameState.value = !editNameState.value }
    ){

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){

            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp),
                text = name,
                style = MaterialTheme.typography.titleLarge,

                )


            
            Text(text = "( $statTotal ): ${StatBonusStandard().Calculate(statTotal)}")
            

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = { swapToSettings() }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Opens Settings Window"
                    )
                }
            }

        }






    }
}


*/