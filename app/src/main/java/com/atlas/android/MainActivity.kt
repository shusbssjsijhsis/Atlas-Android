package com.atlas.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface { AppScreen() }
      }
    }
  }
}

@Composable
private fun AppScreen() {
  var text by remember { mutableStateOf("") }
  val items = remember { mutableStateListOf<String>() }

  Column(Modifier.fillMaxSize().padding(16.dp)) {
    Text("Atlas Android", style = MaterialTheme.typography.headlineSmall)
    Spacer(Modifier.height(12.dp))

    Row(Modifier.fillMaxWidth()) {
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.weight(1f),
        label = { Text("New item") }
      )
      Spacer(Modifier.width(8.dp))
      Button(onClick = {
        val v = text.trim()
        if (v.isNotEmpty()) items.add(0, v)
        text = ""
      }) { Text("Add") }
    }

    Spacer(Modifier.height(16.dp))
    items.forEach { Text("• ") }
  }
}
