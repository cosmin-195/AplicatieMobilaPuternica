package com.example.pleasework

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
                    DisplayLies(view) { lie ->
                        println(lie)
                    }
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Draw(view.testShit.observeAsState("").value, view)
                    }
                }
            }
        }
    }
}

@Composable
fun Draw(s: String, view: LiesViewModel) {
//    Text(text = s)
    val context = LocalContext.current
    Button(onClick = {
        context.startActivity(Intent(context,LieDetailActivity::class.java))
//        view.add()
    }) {
        Text(text = s)
    }

}

@Composable
fun DisplayLies(view: LiesViewModel, selectedItem: (Lie) -> Unit) {
//    val lies = remember { LiesContext.lies }
    val lieList: MutableList<Lie> by view.lies.observeAsState(ArrayList())
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(lieList) { item: Lie ->
           LieListItem(lie = item) {
//                view.remove()
//                view.add()
                println(lieList)
            }
        }
    }

}

class LiesViewModel : ViewModel() {

    val lies: MutableLiveData<MutableList<Lie>> =
        MutableLiveData<MutableList<Lie>>(LiesContext.lies)

    var testShit: MutableLiveData<String> = MutableLiveData("test thing hehe");

    fun changeTestShit() {
        this.testShit.value = "changed test shit"
    }

    fun remove() {
        lies.value?.removeFirst();
    }

    fun add() {
        lies.value?.add(
            Lie(
                "added by view",
                "fda",
                "dfa",
                LieSeverity.MILD,
                ArrayList(),
                ArrayList(),
                "anvc"
            )
        )
        println(lies.value)
    }

}
