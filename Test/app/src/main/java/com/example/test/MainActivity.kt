package com.example.test

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.test.ui.theme.TestTheme
import com.google.androidbrowserhelper.playbilling.provider.Logging
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.io.Files.append

import io.ktor.client.HttpClient
import io.ktor.client.call.body

import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val onDrawComplete: (Bitmap) -> Unit = { bitmap ->
            // Do something with bitmap

        }
        val client = HttpClient(CIO){
                 install(ContentNegotiation) {
                     gson()
                 }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }
            install(Auth){
                bearer {
                    sendWithoutRequest { true }
                }
                }
        }

//CHANGE IP ADDRESS
        val host="http://192.168.0.105:8000"

        setContent {
            TestTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                  Greeting("Darrin", Modifier)
         MyApp(client,host)
                    //SignatureBox()
                   // TestOnl()
                }
            }
        }
    }
}
@Composable
fun MyApp(client: HttpClient,host: String) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Home) {
        composable<Home> { HomeScreen(onNavigateToSettings = { navController.navigate(route = Settings) }) }

        composable<AfterLogin> {
            val args= it.toRoute<AfterLogin>()
            AfterLoginScreen(args,client,host) }
        composable<Settings> { DetailScreen("HEllo", onNavigateToHome = {navController.navigate(route=AfterLogin())}, client = client,host,navController) }
    }
}


@Composable
fun AfterLoginScreen(afterLogin: AfterLogin,client: HttpClient,host: String,){

    val sendtext= remember { mutableStateOf("") }
    val coroutineScope= rememberCoroutineScope()
    Column {
        afterLogin.username?.let { Text(text = it) }

        Text(android.os.Build.DEVICE)

        OutlinedTextField(sendtext.value, onValueChange = {sendtext.value=it})
        Button(onClick = {
            coroutineScope.launch {
                val testtoken = withContext(Dispatchers.IO){
                    afterLogin.username?.let { testtoken(client,host, bearer = it,sendtext.value ) }
                }
            }

        }) { }


    }
}



@Composable
fun HomeScreen(onNavigateToSettings: () -> Unit){
    Column {
        Text("Home")
        Button(onClick = onNavigateToSettings){
            Text("Open settings")
        }
        Text(android.os.Build.DEVICE)




    }
}

@Composable
fun Login(client: HttpClient,host: String,navController: NavController)
{
    val email= remember { mutableStateOf("") }
    val token= remember { mutableStateOf("") }
    val password= remember { mutableStateOf("") }
    val coroutine= rememberCoroutineScope()
    Column {
        OutlinedTextField(email.value, onValueChange = {email.value=it})
        OutlinedTextField(password.value, onValueChange = {password.value=it})
        Button(onClick = {

            coroutine.launch {
                token.value= withContext(Dispatchers.IO){
                  loginuser(Login(email = email.value, password = password.value, device_name = android.os.Build.DEVICE),client,host).bodyAsText()
                }

                navController.navigate(route = AfterLogin(token.value))
            }

        }

        ) { Text("LOGIN") }
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Button(onClick = {},modifier) {
        Text(text = name)
    }
}


@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@Composable
fun DetailScreen(text: String?,onNavigateToHome: () -> Unit,client: HttpClient,host: String,navController: NavController) {


    val testtext= remember{ mutableStateListOf<User>() }

    val coroutine= rememberCoroutineScope()


    fun refreshlist()
    {
        coroutine.launch {
            withContext(Dispatchers.IO) {
                val users = main(client = client,host)
                withContext(Dispatchers.Main) {
                    testtext.clear()
                    testtext.addAll(users)

                }
            }
        }

    }


    LaunchedEffect(Unit) {
        refreshlist()

    }

     Column(Modifier.padding(16.dp)) {

         Button(onClick = onNavigateToHome){
             Text("Open home")
         }
         AddUser ({ refreshlist() },host)


         LazyColumn(
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Center,

         ) {
//            Log.i("lazycol",testtext[0].fname)

             items(testtext) {
                     item->

                 UserDetails(item,client,coroutine,{refreshlist()}, onNavigateToHome = onNavigateToHome, host = host)

             }
         }

         Login(client
         ,host, navController =navController )
     }





//    Button(onClick = onNavigateToHome){
//        Text("Open Home")
//    }
//    Text("${testtext.get(0).fname}")
//
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserDetails(user: User,client: HttpClient,coroutineScope: CoroutineScope,onRefresh: () -> Unit,onNavigateToHome: () -> Unit,host: String) {
    val interactionSource = remember { MutableInteractionSource() }
    val editboolean= remember { mutableStateOf(false) }
    val pressstate = interactionSource.collectIsPressedAsState().value

    val backgroundColor by animateColorAsState(
        if (pressstate) Color.Yellow else Color.Cyan
    )
    Row(

        Modifier
        .clickable(interactionSource,indication=null) {
          editboolean.value=true

        }
        .focusable()
        .background(backgroundColor)
    ){
    Text("${user.fname} ${user.lname}")
    Text(user.email)

//    Button(onClick =
//    {
//        coroutineScope.launch {
//            val test= withContext(Dispatchers.IO){
//                postuser(user,client,host)
//                onRefresh()
//            }
//
//        }
//
//    }
 //   ) { Text("DELETE") }
}
  when
  {
      editboolean.value -> {
          EditAlertDialogExample(
              onDismissRequest = { editboolean.value = false },
              onConfirmation = {
                  editboolean.value = false
                  println("Confirmation registered") // Add logic here to handle confirmation.
              },
              dialogTitle = "Alert dialog example",
              dialogText = "This is an example of an alert dialog with buttons.",
              icon = Icons.Default.Info,
              function = onRefresh,
              host,
              user
          )
      }
  }

}




suspend fun main(client:HttpClient,host:String):List<User> {

    val response: HttpResponse = client.get("${host}/api/users").body()
    //Logger.getLogger(response.toString())
    val js= Json.decodeFromString<List<User>>(response.bodyAsText())
//    Log.i("Check",js[0].fname)


    return Json.decodeFromString<List<User>>(response.bodyAsText())
}



suspend fun postuser(user: User,client: HttpClient,host:String)
{
    val response: HttpResponse = client.post("${host}/api/users") {
        contentType(ContentType.Application.Json)
        setBody(user)
    }

}

suspend fun adduser(user: User,client: HttpClient,host:String)
{
    val response: HttpResponse = client.post("${host}/api/adduser") {
        contentType(ContentType.Application.Json)
        setBody(user)
    }

}
suspend fun edituser(user: User,client: HttpClient,host:String)
{
    val response: HttpResponse = client.put("${host}/api/edituser") {
        contentType(ContentType.Application.Json)
        setBody(user)
    }

}

suspend fun loginuser(login: Login,client: HttpClient,host:String):HttpResponse
{
    val response: HttpResponse = client.post("${host}/api/loginuser") {
        contentType(ContentType.Application.Json)
        setBody(login)
    }
    return response
}

suspend fun testtoken(client: HttpClient,host: String,bearer:String, data:String):HttpResponse
{
    val response: HttpResponse = client.post("${host}/api/testtoken") {

//        headers {
//            append(HttpHeaders.Authorization, "Bearer $bearer")
//            append(HttpHeaders.Accept, "application/json")
//        }
        accept(ContentType.Application.Json)
        headers.append("Authorization","Bearer $bearer")
        contentType(ContentType.Application.Json)
        setBody(User(1,data,data,data,data))
    }
    println(bearer)
    println(response.status)
    println(response.bodyAsText())
    Log.i("Checks",response.headers.toString())
    return response
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestTheme {
        Greeting("Android")
    }
}



// Screen.kt
sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Detail: Screen("detail_screen")
}