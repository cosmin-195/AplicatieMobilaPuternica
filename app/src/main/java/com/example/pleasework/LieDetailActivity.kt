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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pleasework.ui.theme.PleaseWorkTheme

class LieDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = this.intent.getStringExtra("update");
        var toUpdate = LiesContext.lies.find { it.id == id }!!


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
                        UpdateTitle(toUpdate)
                        UpdateText(toUpdate)
                        UpdateSeverity(toUpdate)
                        UpdateTruth(toUpdate = toUpdate)
                        UpdatePeople(toUpdate = toUpdate)
                        UpdateLiesRelatedTo(toUpdate = toUpdate)
                        Update()
                    }
                }
            }
        }
    }
}


@Composable
fun Update() {
    val context = LocalContext.current
    Button(
        onClick = {
            LiesContext.lies.forEach { println(it) }
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
fun UpdateLiesRelatedTo(toUpdate: Lie) {
    var state = remember { mutableStateListOf<Pair<String, Boolean>>() }
    LiesContext.lies.forEach { lie ->
        if (lie.id != toUpdate.id)

            state.add(Pair(lie.title!!, lie.id in toUpdate.liesRelatedTo.map { it.id }))
    }
    Text(text = "Related to:")
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        itemsIndexed(state) { index, item ->
            Row() {
                Checkbox(checked = state[index].second, onCheckedChange = {
                    state[index] = Pair(state[index].first, !state[index].second)
                    if (state[index].second) {
                        LiesContext.lies.find { it.title == state[index].first }
                            ?.let { it1 -> toUpdate.liesRelatedTo.add(it1) }
                    } else {
                        toUpdate.liesRelatedTo.removeIf { it.title == state[index].first }
                    }
                })
                Text(state[index].first)
            }
        }
    }
}

@Composable
fun UpdatePeople(toUpdate: Lie) {
    var count by remember { mutableStateOf(toUpdate.peopleTold.size) }
    Text(text = "People told to:")
    for (i in 0 until count) {
        var state by remember { mutableStateOf(toUpdate.peopleTold.getOrElse(i) { "" }) }
        OutlinedTextField(
            value = state,
            onValueChange = {
                state = it
                if (toUpdate.peopleTold.size < i + 1) {
                    toUpdate.peopleTold.add(it)
                } else {
                    toUpdate.peopleTold[i] = it
                }
            },
            label = { Text("Update people who've heard it") }
        )
    }
    Button(onClick = {
        count++
    }) {
        Text("Add another person")
    }
}