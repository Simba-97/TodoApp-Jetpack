package com.example.doit.todo.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Surface
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Divider
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.doit.ui.theme.DoItTheme
import androidx.lifecycle.lifecycleScope
import com.example.doit.todo.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoActivity: ComponentActivity() {

    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DoItTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AddToolBar()
                }
            }
        }
    }

    @Composable
    fun AddToolBar(){
        Scaffold(
            topBar = {
                TopAppBar (
                    title = {
                        Text(text = "Do It App", color = Color.Black)
                    }
                )
            },
            floatingActionButton = {
                val openDialog = remember { mutableStateOf(false)}
                FloatingActionButton(onClick = { openDialog.value = true}) {
                    AddDialogBox(openDialog = openDialog)
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        ) {
            Recyclerview(todoViewModel = todoViewModel)
        }
    }
    @Composable
    fun AddDialogBox(openDialog: MutableState<Boolean>) {
        val title = remember { mutableStateOf("") }
        val description = remember { mutableStateOf("") }

        if (openDialog.value) {
            AlertDialog(onDismissRequest = {
                openDialog.value = false
            },
                title = {
                    Text(text = "Todo", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = title.value,
                            onValueChange = {
                                title.value = it
                            },
                            placeholder = {
                                Text(text = "Enter title")
                            },
                            label = {
                                Text(text = "Enter title")
                            },
                            modifier = Modifier.padding(10.dp)
                        )
                        OutlinedTextField(
                            value = description.value,
                            onValueChange = {
                                description.value = it
                            },
                            placeholder = {
                                Text(text = "Enter description")
                            },
                            label = {
                                Text(text = "Enter description")
                            },
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                },
                confirmButton = {
                    OutlinedButton(onClick = {
                        insert(title, description = description)
                        openDialog.value = false
                    }) {
                        Text(text = "Save")
                    }
                }
            )
        }
    }
    

    private fun insert(title: MutableState<String>, description: MutableState<String>) {
        lifecycleScope.launchWhenStarted { 
            if(!TextUtils.isEmpty(title.value) && !TextUtils.isEmpty(description.value)) {
                todoViewModel.insert(
                    Todo(
                        title = title.value,
                        description = description.value
                    )
                )
                Toast.makeText(this@TodoActivity, "${title.value} inserted!", Toast.LENGTH_SHORT ).show()
                title.value = ""
                description.value = ""

            } else {
                Toast.makeText(this@TodoActivity, "Please add something..", Toast.LENGTH_SHORT ).show()
            }
        }
    }

    private fun delete(title: String, description: String) {
        lifecycleScope.launchWhenStarted {
            todoViewModel.delete(
                todo = Todo(title = title, description = description)
            )
            //Toast.makeText(this@TodoActivity, "$title deleted!", Toast.LENGTH_SHORT).show()
        }
    }
    
    @Composable
    fun EachRow(todo: Todo){
        
        Card(modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(4.dp),
            backgroundColor = Color.Cyan
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
            ) {
                Text(text = todo.title, fontWeight = FontWeight.ExtraBold)
                Text(text = todo.description, fontStyle = FontStyle.Italic)
            }
        }
    }
    
    @Composable
    fun Recyclerview(todoViewModel: TodoViewModel){
        LazyColumn{
           items(todoViewModel.response.value) { todo ->
               EachRow(todo = todo)
               Button(
                   onClick = {todoViewModel.delete(todo)},
                   modifier = Modifier.offset(9.dp),
                   colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
               ) {
                   Text(text = "Delete Todo", fontWeight = FontWeight.ExtraBold)
               }
               Spacer(modifier = Modifier.height(10.dp))
               Divider(modifier = Modifier.padding(start = 9.dp, end = 9.dp),
                   color = Color.Blue,
                   thickness = 1.dp
               )
           }
        }
    }
}

