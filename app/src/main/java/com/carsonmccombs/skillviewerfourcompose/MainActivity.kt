package com.carsonmccombs.skillviewerfourcompose

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.carsonmccombs.skillviewerfourcompose.stat.Stat
import com.carsonmccombs.skillviewerfourcompose.stat.StatDatabase
import com.carsonmccombs.skillviewerfourcompose.stat.StatEvent
import com.carsonmccombs.skillviewerfourcompose.stat.StatViewModel
import com.carsonmccombs.skillviewerfourcompose.stat.StatViewModelFactory
import com.carsonmccombs.skillviewerfourcompose.ui.StatNavHost
import com.carsonmccombs.skillviewerfourcompose.ui.theme.SkillViewerFourComposeTheme

class MainActivity : ComponentActivity() {



    private val statDB by lazy {
        Room.databaseBuilder(
            applicationContext,
            StatDatabase::class.java,
            "stats.db"
        ).fallbackToDestructiveMigration().build()
    }

    private val statViewModel: StatViewModel by viewModels { StatViewModelFactory(statDB.dao) }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkillViewerFourComposeTheme(dynamicColor = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    /*
                    val popupState = remember { mutableStateOf(true) }
                    IconButton(onClick = { popupState.value = true }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Opens Settings", modifier = Modifier.size(100.dp))
                    }

                    SettingsPopup(enabled = popupState.value, onDismiss = {
                        popupState.value = false
                        Log.d("Twab", "CLOSE")
                    })

                    */
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(
                                shape = CircleShape,
                                onClick = {statViewModel.onEvent(StatEvent.Upsert(stat = Stat(name = "STAT NAME")))},
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            ) {
                                Icon(
                                    modifier = Modifier.size(64.dp),
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Adds Empty Stat")
                            }
                        }
                    ){ paddingValues ->
                        StatList(paddingValues)
                    }


                }
            }
        }
    }




    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_Z){
            statViewModel.onEvent(StatEvent.Upsert(stat = Stat(name = "STAT NAME")))
            Log.d("Twab","test")
        }
        return super.onKeyDown(keyCode, event)
    }




    @Composable
    fun StatList(paddingValues: PaddingValues){
        val ids =  statViewModel.getStatIDs().collectAsState(initial = emptyList())
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                items(
                    items = ids.value
                    ) { statID ->
                    StatNavHost(statID = statID, viewmodel = statViewModel)

                }
            }

        }
    }



}

