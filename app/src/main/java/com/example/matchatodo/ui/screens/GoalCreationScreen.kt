package com.example.matchatodo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matchatodo.ui.theme.*

@Composable
fun GoalCreationScreen(
    onBack: () -> Unit,
    onSave: (String, List<String>) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf("")) }

    Column(Modifier.fillMaxSize().background(BgCream).padding(24.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }

        Text("New Goal", fontSize = 32.sp, fontWeight = FontWeight.Black, color = TextEspresso)
        Spacer(Modifier.height(24.dp))

        Text("GOAL NAME", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted)
        BasicTextField(
            value = name, onValueChange = { name = it },
            textStyle = TextStyle(fontSize = 18.sp, color = TextEspresso),
            modifier = Modifier.fillMaxWidth().background(White, RoundedCornerShape(12.dp)).border(2.dp, TextEspresso, RoundedCornerShape(12.dp)).padding(16.dp)
        )

        Spacer(Modifier.height(24.dp))
        Text("TASKS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted)

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(tasks) { index, task ->
                Row {
                    Text("${index+1}.", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top=12.dp))
                    Spacer(Modifier.width(8.dp))
                    BasicTextField(
                        value = task, onValueChange = { new ->
                            val list = tasks.toMutableList(); list[index] = new; tasks = list
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.weight(1f).background(White, RoundedCornerShape(8.dp)).border(1.dp, TextEspresso, RoundedCornerShape(8.dp)).padding(12.dp)
                    )
                }
            }
            item {
                Button(onClick = { tasks = tasks + "" }, colors = ButtonDefaults.buttonColors(containerColor = Terracotta), modifier = Modifier.fillMaxWidth().padding(top=8.dp)) {
                    Text("Add Task +")
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = { if(name.isNotBlank()) onSave(name, tasks.filter { it.isNotBlank() }) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MatchaGreen)
        ) {
            Text("Start Brewing", fontSize = 18.sp)
        }
    }
}