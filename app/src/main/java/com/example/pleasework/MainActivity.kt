package com.example.pleasework

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
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
        var view = ViewModelProvider(this).get(LiesViewModel::class.java)

        setContent {
            PleaseWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DisplayLies(view.lies) { lie ->
                        println(lie)
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayLies(list:MutableLiveData<MutableList<Lie>> , selectedItem: (Lie) -> Unit) {
//    val lies = remember { LiesContext.lies }
    val lieList by list.observeAsState(initial = LiesContext.lies)
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(lieList) { item: Lie ->
            lieListItem(lie = item) {
                lieList.removeFirst()
            }
        }
    }

}

class LiesViewModel : ViewModel() {

    val lies: MutableLiveData<MutableList<Lie>> = MutableLiveData<MutableList<Lie>>(LiesContext.lies)

    fun remove(){
        lies.value?.removeFirst();
    }

}
