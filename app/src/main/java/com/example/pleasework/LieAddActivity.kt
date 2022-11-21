package com.example.pleasework

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pleasework.ui.theme.PleaseWorkTheme

class LieAddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var toAdd = Lie(null, null, null, null, ArrayList(), ArrayList(), null)
        setContent {
            PleaseWorkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AddTitle(toAdd)
                        AddText(toAdd)
                        AddSeverity(toAdd)
                        AddTruth(toAdd = toAdd)
                        AddPeople(toAdd = toAdd)
                        AddLiesRelatedTo(toAdd = toAdd)
                        Save(toSave = toAdd)
                    }
                }
            }
        }
    }
}

@Composable
fun Save(toSave: Lie) {
    val context = LocalContext.current
    Button(
        onClick = {
            LiesContext.lies.add(toSave)
            LiesContext.lies.forEach { println(it) }
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
fun AddLiesRelatedTo(toAdd: Lie) {
    var text by remember { mutableStateOf("") }
    var state = remember { mutableStateListOf<Pair<String, Boolean>>() }
    LiesContext.lies.forEach {
        if (it.id != toAdd.id)
            state.add(Pair(it.title!!, false))
    }
    Text(text = "Related to:")
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(state) { index, item ->
            Checkbox(checked = state[index].second, onCheckedChange = {
                state[index] = Pair(state[index].first, !state[index].second)
                if (state[index].second) {
                    LiesContext.lies.find { it.title == state[index].first }
                        ?.let { it1 -> toAdd.liesRelatedTo.add(it1) }
                } else {
                    toAdd.liesRelatedTo.removeIf { it.title == state[index].first }
                }
            })
            Text(state[index].first)
        }
    }
}

@Composable
fun AddPeople(toAdd: Lie) {
    var count by remember { mutableStateOf(1) }
    Text(text = "People told to:")
    for (i in 0 until count) {
        var state by remember { mutableStateOf("") }
        OutlinedTextField(
            value = state,
            onValueChange = {
                state = it
                if (toAdd.peopleTold.size < i + 1) {
                    toAdd.peopleTold.add(it)
                } else {
                    toAdd.peopleTold[i] = it
                }
            },
            label = { Text("Add people who've heard it") }
        )
    }
    Button(onClick = {
        count++
    }) {
        Text("Add new")
    }
}