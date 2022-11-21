package com.example.pleasework

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pleasework.ui.theme.PleaseWorkTheme

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
                    val lieList: MutableList<Lie> by view.lies.observeAsState(ArrayList())
                    DisplayLies(lieList, view) { lie ->
                        println(lie)
                    }
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
fun DisplayLies(lieList: MutableList<Lie>, viewModel: LiesViewModel, selectedItem: (Lie) -> Unit) {
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

class LiesViewModel : ViewModel() {

    val lies: MutableLiveData<MutableList<Lie>> =
        MutableLiveData<MutableList<Lie>>(LiesContext.lies)

    fun remove() {
        lies.value?.removeFirst();
    }

    fun remove(id: String) {
        lies.value?.removeIf { it.id == id }
    }

    fun add() {
    }

}
