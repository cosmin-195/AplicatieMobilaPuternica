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
                    val lieList: List<LieWithLies>? by view.lies.observeAsState()
                    val relatedToIds = ArrayList<Int>()

                    println("this=$toUpdate")
                    println("All lies=$lieList")


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
                        toUpdate?.lie?.let {
                            UpdateLiesRelatedTo(
                                toUpdate!!,
                                lieList!!,
                                relatedToIds
                            )
                        }
                        Update(service, toUpdate)
                    }
                }
            }
        }
    }
}


@Composable
fun Update(service: LieService, toUpdate: LieWithLies?) {
    val context = LocalContext.current
    Button(
        onClick = {
            service.update(toUpdate!!.lie)
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
fun UpdateLiesRelatedTo(
    toUpdate: LieWithLies,
    lies: List<LieWithLies>,
    relatedToIds: MutableList<Int>
) {
    val state = remember { mutableStateListOf<LieCheckbox>() }
    lies.forEach { lie ->
        state.add(
            LieCheckbox(
                lie.lie.id!!,
                toUpdate.relatedTo.map { it.lieId }.contains(lie.lie.id),
                lie.lie.title!!
            )
        )
    }
    println("state=${state.toList()}")
    Text(text = "Related to:")
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
    ) {
        itemsIndexed(state) { index, item ->
            Row() {
                Checkbox(checked = state[index].checked, onCheckedChange = { newValue ->
                    println("changed  " + state[index] + " to " + newValue.toString())
                    state[index] = state[index].copy(checked = newValue)
                    println(state[index])
                    if (state[index].checked) {
                        relatedToIds.add(state[index].id)
                        println(relatedToIds)
                    } else {
                        relatedToIds.removeIf { it == state[index].id }
                    }
                })
                Text(state[index].title)
            }
        }
    }
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

    var lies: LiveData<List<LieWithLies>> = fetchLies()

    fun fetchLies(): LiveData<List<LieWithLies>> {
        return service.repository.getAllLiesWithRelations()
    }

    fun fetchRelations(): LiveData<List<Int>>? {
        return this.lie?.value?.lie?.id?.let { service.repository.getRelationsByLieId(it) }
    }
}