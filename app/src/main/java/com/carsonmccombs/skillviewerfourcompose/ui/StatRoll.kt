package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.carsonmccombs.skillviewerfourcompose.R
import com.carsonmccombs.skillviewerfourcompose.statbonus.StatBonusStandard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatRoll(id: Int, statName:String, statTotal: Int){
    
    val snackbarHostState = remember(id, "snackbarHostState"){ SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val statBonus = StatBonusStandard().Calculate(statTotal)
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)},
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSecondary


    ) {paddingValues ->
        Row(
            modifier = Modifier.padding(paddingValues),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            RollDiceButton(
                coroutineScope = scope,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.weight(1f),
                statName = statName,
                statBonus = statBonus
            )
            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Stat Total: $statTotal")
                Text("Bonus To Rolls: $statBonus")
            }
        }
    }



}

@Composable
fun RollDiceButton(coroutineScope: CoroutineScope, snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier, statName: String, statBonus: Int){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier
                .aspectRatio(1f)
                .size(80.dp)
                .clip(DiceOutlineShape())
                .background(Color.Black),

            onClick = {
                coroutineScope.launch {
                    val diceResult = (Random().nextInt(20) + 1)
                    val rollTotal = diceResult + statBonus
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = "Result ( $statName ): $diceResult + $statBonus = $rollTotal") }
            }
        ){
            Icon(modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),painter = painterResource(id = R.drawable.drawable_d20_fulloutline_filled), contentDescription = "Rolls a D20 Plus Stat Total", tint = MaterialTheme.colorScheme.tertiary)//(drawable_d20_fulloutline_filled),

        }
    }
}



fun DiceOutlineShape(): GenericShape {
    return GenericShape{ size, _ ->
        this.apply {
            moveTo(0f, size.height / 2f)
            lineTo(size.width * 0.25f, size.height * 0.933f )
            lineTo(size.width * 0.75f, size.height * 0.933f )
            lineTo(size.width, size.height/2f)
            lineTo(size.width * 0.75f, size.height * 0.067f )
            lineTo(size.width * 0.25f, size.height * 0.067f )
            lineTo(0f, size.height / 2f)
            close()
        }

    }
}
/*
M 0, 50L
             75, 93.3L
              25, 93.3L
               0, 50L
                25, 13.4L
                 75, 13.4.6z" />
 */