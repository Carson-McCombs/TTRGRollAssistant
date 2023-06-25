package com.carsonmccombs.skillviewerfourcompose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.navigation.NavHostController
import com.carsonmccombs.skillviewerfourcompose.stat.StatViewModel
import com.carsonmccombs.skillviewerfourcompose.statbonus.StatBonusStandard
import com.carsonmccombs.skillviewerfourcompose.statmodifier_relationship.StatWithModifiers
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatNavHost(
    statID: Int,
    viewmodel: StatViewModel,
    modifier: Modifier = Modifier,
    //navController: NavHostController = rememberNavController(),
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = "rollable"
){
    val statWithModifiers: State<StatWithModifiers?> = viewmodel.getStatWithModifiers(statID = statID).collectAsState(
        StatWithModifiers()
    )
    if (statWithModifiers.value == null) return

    val stat = statWithModifiers.value!!.stat
    val statTotal = viewmodel.statTotals.collectAsState(initial = emptyMap()).value[stat.id]?: 0
    //Log.d("Twab", "$statID : $statTotal")
    //val statModifiers = statWithModifiers.value.modifiers

    val navIndex = remember(statID, "navIndex"){ mutableStateOf(0) }

    val navLocations = arrayOf("rollable","modifiers","settings")

    val showNavigationArrows = remember{ mutableStateOf(false) }


    CollapsibleCard(
        id = statID,
        onStateChange = {showNavigationArrows.value = it},
        titleBar = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(disabledContainerColor = Color.Transparent, disabledContentColor = Color.Transparent),
                    enabled = (navIndex.value > 0) && !showNavigationArrows.value,
                    onClick = {
                        if (navIndex.value > 0) {
                            navIndex.value --
                            when (navIndex.value){
                                1 -> navController.navigate("settings/modifiers")
                                0 -> navController.navigate("rollable")
                            }
                            //navController.navigate(navLocations[navIndex.value])
                        }
                    }
                ){
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Navigates to the Left Page")
                }
                Text(text = "${stat.name} ( $statTotal ): ${StatBonusStandard().Calculate(statTotal)}")
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(disabledContainerColor = Color.Transparent, disabledContentColor = Color.Transparent),
                    enabled = navIndex.value < 2 && !showNavigationArrows.value,
                    onClick = {
                        if (navIndex.value < 2) {
                            navIndex.value ++
                            when (navIndex.value){
                                1 -> navController.navigate("rollable/modifiers")
                                2 -> navController.navigate("settings")
                            }
                            //navController.navigate(navLocations[navIndex.value])
                        }
                    }
                ){
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Navigates to the Right Page")
                }
            }
        }
    ) {
        AnimatedNavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ){
            composable(
                route = "rollable",
                enterTransition = { slideInHorizontally(animationSpec = tween(500), initialOffsetX = {-it}).plus(fadeIn(animationSpec = tween(500))) },
                exitTransition = { slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {it}).plus(fadeOut(animationSpec = tween(500))) },
                ){
                StatRoll(statID, stat.name, statTotal)
            }
            composable(
                route = "rollable/modifiers",
                enterTransition = { slideInHorizontally(animationSpec = tween(500), initialOffsetX = {it}).plus(fadeIn(animationSpec = tween(500))) },
                exitTransition = { slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {-it}).plus(fadeOut(animationSpec = tween(500))) },
            ){
                StatsCompose(
                    stat = stat,
                    viewmodel = viewmodel,
                )
            }
            composable(
                route = "settings/modifiers",
                enterTransition = { slideInHorizontally(animationSpec = tween(500), initialOffsetX = {-it}).plus(fadeIn(animationSpec = tween(500))) },
                exitTransition = { slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {it}).plus(fadeOut(animationSpec = tween(500))) },
            ){
                StatsCompose(
                    stat = stat,
                    viewmodel = viewmodel,
                )
            }
            composable(
                route = "settings",
                enterTransition = { slideInHorizontally(animationSpec = tween(500), initialOffsetX = {it}).plus(fadeIn(animationSpec = tween(500))) },
                exitTransition = { slideOutHorizontally(animationSpec = tween(500), targetOffsetX = {-it}).plus(fadeOut(animationSpec = tween(500))) },
            ){
                StatSettings (
                    stat = stat, onEvent = viewmodel::onEvent
                )
            }
    }

    }
}


@Composable
fun CollapsibleCard(id: Int, onStateChange: (Boolean) -> Unit, titleBar: @Composable (ColumnScope.() -> Unit), content: @Composable (ColumnScope.() -> Unit)){
    val isCollapsed = remember(id, "collapsedState") { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clickable {
                    isCollapsed.value = !isCollapsed.value
                    onStateChange(isCollapsed.value)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            titleBar()
        }
        AnimatedVisibility(visible = !isCollapsed.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .heightIn(max = if (isCollapsed.value) 0.dp else 400.dp)
            ){
                content()
            }
        }


    }

}

fun EnterTransition_SlideRight(){

}