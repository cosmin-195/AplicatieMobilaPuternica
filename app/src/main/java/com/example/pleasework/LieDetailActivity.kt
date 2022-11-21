package com.example.pleasework

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.pleasework.ui.theme.PleaseWorkTheme
import kotlin.text.Typography

class LieDetailActivity : ComponentActivity() {
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
//        .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            severity.toString(), modifier = Modifier
//            .fillMaxWidth()
                .clickable(onClick = { expanded = true })
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
//                .fillMaxWidth()
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
fun AddPeople(toAdd: Lie) {
    var state by remember { mutableStateOf("") }
    var text by remember { mutableStateOf(ArrayList<String>()) }
    Text(text = "People told to:")
    Text(text = text.joinToString(", "))
    OutlinedTextField(
        value = state,
        onValueChange = {
            state = it
        },
//        modifier = Modifier.on { text.add(state) },
        label = { Text("Add people who've heard it") }
    )
}