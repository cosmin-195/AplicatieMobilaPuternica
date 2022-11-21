package com.example.pleasework

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.unit.dp

@Composable
fun LieListItem(lie: Lie, selectedItem: (Lie) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(corner = CornerSize(10.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .clickable { selectedItem(lie) },
            verticalAlignment = Alignment.CenterVertically
        ) {
//            lie.id?.let { Text(text = it) }
//            val b = ActivityOptions.makeBasic().toBundle()
//            b.putString("toUpdate", lie.id)
            lie.title?.let {
                val intent = Intent(context, LieDetailActivity::class.java)
                intent.putExtra("update", lie.id)
                ClickableText(text = AnnotatedString(it, ParagraphStyle())) {
                    context.startActivity(intent)
                }
            }
//            Text(text = lie.severity.toString())
//            lie.text?.let { Text(text = it) }
//            lie.truth?.let { Text(text = it) }
            Button(onClick = { lie.apply(selectedItem) }
            ) {
                Text("DELETE")
            }
        }
    }
}

