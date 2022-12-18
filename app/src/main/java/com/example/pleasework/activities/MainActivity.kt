package com.example.pleasework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pleasework.activities.LieAddActivity
import com.example.pleasework.business.LieService
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieWithLies
import com.example.pleasework.ui.theme.PleaseWorkTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = ViewModelProvider(this)[LiesViewModel::class.java]

        setContent {
            PleaseWorkTheme {
                // A surface container using the "background" color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val lieList: List<Lie>? by view.lies.observeAsState()
                    lieList?.let {
                        DisplayLies(it, view) { lie ->
                            println(lie)
                        }
                    }
//                    val l: Lie? by view.getLieById(19).observeAsState()
//                    val l2: LieWithLies? by view.getLieWithRelationById(19).observeAsState()
//                    println("l=$l")
//                    println("l2=$l2")
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Draw()
                    }
                }
            }
        }
    }
}

@Composable
fun Draw() {
    val context = LocalContext.current
    Button(onClick = {
        context.startActivity(Intent(context, LieAddActivity::class.java))
    }) {
        Text(text = "Add a new lie")
    }

}

@Composable
fun DisplayLies(
    lieList: List<Lie>,
    viewModel: LiesViewModel,
    selectedItem: (Lie) -> Unit
) {
    var deleted by remember { mutableStateOf(lieList.count()) }

    Column {
        Log.d("Main", "Deleted: " + deleted)
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(lieList) { item: Lie ->
                LieListItem(lie = item) {
                    deleted++
                    viewModel.remove(item.id!!)
                    println(lieList)
                }
            }
        }
    }


}

@HiltViewModel
class LiesViewModel @Inject constructor(private val service: LieService) : ViewModel() {

    var lies: LiveData<List<Lie>> = fetchLies()

    fun fetchLies(): LiveData<List<Lie>> {
        return service.getAll()
    }


    fun remove(id: Int) {
        service.remove(id)
    }

    fun getLieWithRelationById(id: Int):LiveData<LieWithLies>{
        return service.getLieWithRelationsById(id)
    }

    fun add(lie: Lie) {
        service.add(lie)
    }

    fun getLieById(id: Int): LiveData<Lie>{
        return service.repository.getLieById(id)
    }

}
