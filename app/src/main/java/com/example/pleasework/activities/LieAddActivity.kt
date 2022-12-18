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
import com.example.pleasework.MainActivity
import com.example.pleasework.business.LieService
import com.example.pleasework.domain.Lie
import com.example.pleasework.domain.LieId
import com.example.pleasework.domain.LieSeverity
import com.example.pleasework.domain.LieWithLies
import com.example.pleasework.ui.theme.PleaseWorkTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LieAddActivity : ComponentActivity() {

    @Inject
    lateinit var service: LieService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var toAdd = LieWithLies(Lie(null, "", "", LieSeverity.MILD, ""), ArrayList())
        setContent {
            PleaseWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val lieList: List<Lie>? by service.getAll().observeAsState()
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AddTitle(toAdd.lie)
                        AddText(toAdd.lie)
                        AddSeverity(toAdd.lie)
                        AddTruth(toAdd = toAdd.lie)
//                        AddPeople(toAdd = toAdd)
                        lieList?.let { AddLiesRelatedTo(toAdd, it) }
                        Save(toAdd, service)
                    }
                }
            }
        }
    }
}

@Composable
fun Save(toSave: LieWithLies, service: LieService) {
    val context = LocalContext.current
    Button(
        onClick = {
            service.insertLieWithRelations(toSave)
            context.startActivity(Intent(context, MainActivity::class.java))
        },
    ) {
        Text(text = "Save")
    }
}

@Composable
fun AddTitle(toAdd: Lie) {
    var text by remember { mutableStateOf("") }
    Text(text = "Title")
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            toAdd.title = it
        },
        label = { Text("Add a title") }
    )
}

@Composable
fun AddText(toAdd: Lie) {
    var text by remember { mutableStateOf("") }
    Text(text = "Text")
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            toAdd.text = it
        },
        label = { Text("Add content") }
    )
}

@Composable
fun AddSeverity(toAdd: Lie) {
    var severity by remember { mutableStateOf(LieSeverity.MILD) }
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
                    toAdd.severity = s
                }) {
                    Text(text = s.toString())
                }
            }
        }
    }
}

@Composable
fun AddTruth(toAdd: Lie) {
    var text by remember { mutableStateOf("") }
    Text(text = "Truth")
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            toAdd.truth = it
        },
        label = { Text("The truth") }
    )
}

@Composable
fun AddLiesRelatedTo(toUpdate: LieWithLies, lies: List<Lie>) {
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
                Checkbox(checked = state[index].checked, onCheckedChange = { newValue          ->
                    println("changedd  " +state[index] + " to" + newValue.toString())
                    state[index] = state[index].copy(checked = newValue)
                    if (state[index].checked) {
                        toUpdate.lie.id?.let { it1 ->
                            LieId(
                                state[index].id,
                                it1
                            )
                        }?.let { it2 -> toUpdate.relatedTo.add(it2) }
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
    fun AddPeople(toAdd: Lie) {
//    var count by remember { mutableStateOf(1) }
//    Text(text = "People told to:")
//    for (i in 0 until count) {
//        var state by remember { mutableStateOf("") }
//        OutlinedTextField(
//            value = state,
//            onValueChange = {
//                state = it
//                if (toAdd.peopleTold.size < i + 1) {
//                    toAdd.peopleTold.add(it)
//                } else {
//                    toAdd.peopleTold[i] = it
//                }
//            },
//            label = { Text("Add people who've heard it") }
//        )
//    }
//    Button(onClick = {
//        count++
//    }) {
//        Text("Add new")
    }