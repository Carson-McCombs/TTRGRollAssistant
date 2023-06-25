package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carsonmccombs.skillviewerfourcompose.stat.Stat
import com.carsonmccombs.skillviewerfourcompose.stat.StatEvent




@Composable
fun StatSettings(stat: Stat, onEvent: (StatEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        StatNameTextField(stat = stat, onEvent = onEvent)
        StatDeleteButton(
            modifier = Modifier.weight(1f),
            stat = stat,
            onEvent = onEvent,
            //swapToStat = swapToStat
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}


@Composable
fun StatDeleteButton(modifier: Modifier = Modifier, stat: Stat, onEvent: (StatEvent) -> Unit) {
    val confirmState = remember(stat.id, "confirmDelete") { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = if (confirmState.value) Color.Red else Color.LightGray
            ),
            onClick = {
                if (!confirmState.value) {
                    confirmState.value = true
                } else {
                    //swapToStat() // used to reset navController state and have it be ready for the next stat
                    onEvent(StatEvent.Delete(stat))

                }
            }
        ) {
            Text(text = if (confirmState.value) "CONFIRM DELETE" else "DELETE ${stat.name}")
        }
    }

}

@Composable
fun StatNameTextField(stat: Stat, onEvent: (StatEvent) -> Unit) {

    val nameState = remember(stat.id, "name") { mutableStateOf(stat.name) }
    //var name = stat.name
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .wrapContentSize(),
            text = "Stat Name: ",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onSecondary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                ),
            value = nameState.value,//name,
            onValueChange = { text ->
                nameState.value = text
                //name = text
                onEvent(StatEvent.Upsert(stat.copy(name = nameState.value)))//name)))
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
        )
    }

}
/*
@Composable
fun StatSettingsCard_TopBar(stat: Stat , /*onEvent: (StatEvent) -> Unit,*/ swapToStat: () -> Unit){
    //val nameState = remember { mutableStateOf(stat.name) }
    Card(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ){


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "${stat.name} ( Settings )", style = MaterialTheme.typography.titleLarge)

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = { swapToStat() }
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(1f),
                        imageVector = Icons.Default.Close,
                        contentDescription = "Closes Settings Window"
                    )
                }

            }
            Spacer(modifier = Modifier.width(8.dp))
        }






    }
}
*/

@Preview
@Composable
fun StatSettingsCard_Preview(){
    StatSettings (stat = Stat(), {})
}