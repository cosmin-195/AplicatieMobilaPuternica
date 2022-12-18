package com.example.pleasework.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pleasework.MainActivity
import com.example.pleasework.business.LieService
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieId
import com.example.pleasework.domain.LieSeverity
import com.example.pleasework.domain.LieWithLies
import com.example.pleasework.ui.theme.PleaseWorkTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LieDetailActivity : ComponentActivity() {

    @Inject
    lateinit var service: LieService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = this.intent.getIntExtra("update", 0)

        val view = ViewModelProvider(this)[LieDetailViewModel::class.java]
        view.fetchLie(id)

        setContent {
            PleaseWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val toUpdate: LieWithLies? by view.lie!!.observeAsState()
                    val lieList: List<Lie>? by view.lies.observeAsState()
                    println("a=$toUpdate")
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        toUpdate?.lie?.let { UpdateTitle(it) }
                        toUpdate?.lie?.let { UpdateText(it) }
                        toUpdate?.lie?.let { UpdateSeverity(it) }
                        toUpdate?.lie?.let { UpdateTruth(it) }
//                        UpdatePeople(toUpdate = toUpdate)
                        toUpdate?.lie?.let { UpdateLiesRelatedTo(toUpdate!!, lieList!!) }
                        Update(service)
                    }
                }
            }
        }
    }
}


@Composable
fun Update(service: LieService) {
    val context = LocalContext.current
    Button(
        onClick = {
//            LiesContext.legacyLies.forEach { println(it) }
//            viewModel.add() // todo .update(lie, liesRelatedTo: List<id> 
            context.startActivity(Intent(context, MainActivity::class.java))
        },
    ) {
        Text(text = "Save")
    }
}

@Composable
fun UpdateTitle(toUpdate: Lie) {
    var text by remember { mutableStateOf(toUpdate.title!!) }
    Text(text = "Title")
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            toUpdate.title = it
        },
        label = { Text("Update title") }
    )
}

@Composable
fun UpdateText(toUpdate: Lie) {
    var text by remember { mutableStateOf(toUpdate.text!!) }
    Text(text = "Text")
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            toUpdate.text = it
        },
        label = { Text("Update content") }
    )
}

@Composable
fun UpdateSeverity(toUpdate: Lie) {
    println("toUpdate:$toUpdate")
    var severity by remember { mutableStateOf(toUpdate.severity!!) }
    var expanded by remember { mutableStateOf(false) }
    val items = LieSeverity.values()
    Text(text = "Severity")
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            severity.toString(), modifier = Modifier
                .clickable(onClick = { expanded = true })
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(
                    Color.Red
                )
        ) {
            items.forEach { s ->
                DropdownMenuItem(onClick = {
                    severity = s
                    expanded = false
                    toUpdate.severity = s
                }) {
                    Text(text = s.toString())
                }
            }
        }
    }
}

@Composable
fun UpdateTruth(toUpdate: Lie) {
    var text by remember { mutableStateOf(toUpdate.truth!!) }
    Text(text = "Truth")
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            toUpdate.truth = it
        },
        label = { Text("The truth") }
    )
}

@Composable
fun UpdateLiesRelatedTo(toUpdate: LieWithLies, lies: List<Lie>) {
    val state = remember { mutableStateListOf<LieCheckbox>() }
    lies.forEach { lie ->
        state.add(
            LieCheckbox(
                lie.id!!,
                lie.id in toUpdate.relatedTo.map { it.parentLie },
                lie.title!!
            )
        )
    }
    Text(text = "Related to:")
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        itemsIndexed(state) { index, item ->
            Row() {
                Checkbox(checked = state[index].checked, onCheckedChange = {
                    println("changedd ${state[index]} to ${state[index].checked}")
                    state[index] = state[index].copy(checked = !state[index].checked)
                    if (state[index].checked) {
                        toUpdate.lie.id?.let { it1 ->
                            LieId(
                                state[index].id,
                                it1
                            )
                        }?.let { it2 -> toUpdate.relatedTo.add(it2) }
                        println(toUpdate.relatedTo)
                    } else {
                        toUpdate.relatedTo.removeIf { it.lieId == state[index].id }
                    }
                })
                Text(state[index].title)
            }
        }
    }
}

@Composable
fun UpdatePeople(toUpdate: Lie) {
//    var count by remember { mutableStateOf(toUpdate.peopleTold.size) }
//    Text(text = "People told to:")
//    for (i in 0 until count) {
//        var state by remember { mutableStateOf(toUpdate.peopleTold.getOrElse(i) { "" }) }
//        OutlinedTextField(
//            value = state,
//            onValueChange = {
//                state = it
//                if (toUpdate.peopleTold.size < i + 1) {
//                    toUpdate.peopleTold.add(it)
//                } else {
//                    toUpdate.peopleTold[i] = it
//                }
//            },
//            label = { Text("Update people who've heard it") }
//        )
//    }
//    Button(onClick = {
//        count++
//    }) {
//        Text("Add another person")
//    }
}

data class LieCheckbox(
    val id: Int,
    var checked: Boolean,
    val title: String
)

@HiltViewModel
class LieDetailViewModel @Inject constructor(private val service: LieService) : ViewModel() {

    var lie: LiveData<LieWithLies>? =
        MutableLiveData(LieWithLies(Lie(-1, "", "", LieSeverity.MILD, ""), ArrayList()))

    fun fetchLie(id: Int) {
        lie = service.getLieWithRelationsById(id)
    }

    var lies: LiveData<List<Lie>> = fetchLies()

    fun fetchLies(): LiveData<List<Lie>> {
        return service.getAll()
    }

}