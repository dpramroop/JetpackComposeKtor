package com.example.test

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.vector.ImageVector
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddUser(function: () -> Unit, host: String)
{
    val addboolean= remember { mutableStateOf(false) }

    Button(onClick = {addboolean.value=true }) {
        Text("Open Add User Dialog")
    }

    when {
        // ...
        addboolean.value -> {
            AlertDialogExample(
                onDismissRequest = { addboolean.value = false },
                onConfirmation = {
                    addboolean.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                },
                dialogTitle = "Alert dialog example",
                dialogText = "This is an example of an alert dialog with buttons.",
                icon = Icons.Default.Info,
                function = function,
                host
            )
        }
    }
}



@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    function: () -> Unit,
    host:String,
    user: User?=null
) {
    val addfname= remember { mutableStateOf("") }
    val addlname= remember { mutableStateOf("") }
    val addemail= remember { mutableStateOf("") }
    val addpassword= remember { mutableStateOf("") }

          if (user != null) {
              addfname.value=user.fname
          }


    val client = HttpClient(CIO){
        install(ContentNegotiation) {
            gson()
        }
    }
    val coroutine= rememberCoroutineScope()
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column{
                Text(text = dialogText)
                OutlinedTextField(addfname.value,{addfname.value=it})
                OutlinedTextField(addlname.value,{addlname.value=it})
                OutlinedTextField(addemail.value,{addemail.value=it})
                OutlinedTextField(addpassword.value,{addpassword.value=it})
            }

        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutine.launch {
                        val test= withContext(Dispatchers.IO){
                            adduser(User(fname = addfname.value,lname=addlname.value, email = addemail.value ,password = addpassword.value),client,host)
                            function()
                            onDismissRequest()
                        }

                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun EditAlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    function: () -> Unit,
    host:String,
    user: User?=null
) {
    val addfname= remember { mutableStateOf("") }
    val addlname= remember { mutableStateOf("") }
    val addemail= remember { mutableStateOf("") }
    val addpassword= remember { mutableStateOf("") }

    if (user != null) {
        addfname.value=user.fname
        addlname.value=user.lname
        addemail.value=user.email
    }


    val client = HttpClient(CIO){
        install(ContentNegotiation) {
            gson()
        }
    }
    val coroutine= rememberCoroutineScope()
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column{
                Text(text = dialogText)
                OutlinedTextField(addfname.value,{addfname.value=it})
                OutlinedTextField(addlname.value,{addlname.value=it})
                OutlinedTextField(addemail.value,{addemail.value=it})
//                OutlinedTextField(addpassword.value,{addpassword.value=it})
            }

        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    coroutine.launch {
                        val test= withContext(Dispatchers.IO){
                            edituser(User(id=user?.id,fname = addfname.value,lname=addlname.value, email = addemail.value ,password = addpassword.value),client,host)
                            function()
                            onDismissRequest()
                        }

                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
