package com.appcoding.social

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.models.SigninRequest
import com.appcoding.social.models.SignupRequest
import com.appcoding.social.ui.theme.SocialTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception


class SignUpIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialTheme {
                val navController = rememberNavController()
                AppNavHost(navController,"splash")
            }
        }
    }
}

@Composable
fun Splash(navHostController: NavHostController){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.appcolor)
    )

    LaunchedEffect(Unit) {
        delay(5000)
        navHostController.navigate("signin"){
            popUpTo("splash") { inclusive = true }
        }


    }
}

@Composable
fun SignUp(navController: NavHostController){

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current
    var signup by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    var message by remember { mutableStateOf("") }


    RightToLeftLayout{

        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) })
        {contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Colors.signup_background_top,
                                Colors.signup_background_bottom
                            )
                        )
                    )
                    .padding(Dimens.login_padding),
                contentAlignment = Alignment.Center
            )
            {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = RoundedCornerShape(Dimens.login_card_corner),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "close signup",
                                tint = Colors.close_color,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        navController.navigate("signin"){
                                            popUpTo("signup"){inclusive = true}
                                        }
                                    },
                            )
                        }


                        Spacer(modifier = Modifier.size(15.dp))

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = phone,
                            onValueChange = { phone = it },
                            shape = RoundedCornerShape(Dimens.textfield_corner),
                            placeholder = { Text(text = "شماره موبایل") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall,
                            colors = TextFieldDefaults.colors(
                                focusedPlaceholderColor = Colors.outline_textfield_border,
                                unfocusedPlaceholderColor = Colors.outline_textfield_border,
                                focusedIndicatorColor = Colors.outline_textfield_border,
                                unfocusedIndicatorColor = Colors.outline_textfield_border
                            )

                        )

                        Spacer(modifier = Modifier.size(Dimens.login_spacer))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = { username = it },
                            shape = RoundedCornerShape(Dimens.textfield_corner),
                            placeholder = { Text(text = "نام کاربری") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall,
                            colors = TextFieldDefaults.colors(
                                focusedPlaceholderColor = Colors.outline_textfield_border,
                                unfocusedPlaceholderColor = Colors.outline_textfield_border,
                                focusedIndicatorColor = Colors.outline_textfield_border,
                                unfocusedIndicatorColor = Colors.outline_textfield_border
                            )
                        )

                        Spacer(modifier = Modifier.size(Dimens.login_spacer))


                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            shape = RoundedCornerShape(Dimens.textfield_corner),
                            placeholder = { Text(text = "رمز عبور") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall,
                            colors = TextFieldDefaults.colors(
                                focusedPlaceholderColor = Colors.outline_textfield_border,
                                unfocusedPlaceholderColor = Colors.outline_textfield_border,
                                focusedIndicatorColor = Colors.outline_textfield_border,
                                unfocusedIndicatorColor = Colors.outline_textfield_border
                            )
                        )

                        Spacer(modifier = Modifier.size(Dimens.login_spacer))

                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            Button(
                                modifier = Modifier
                                    .height(Dimens.signup_button_height)
                                    .fillMaxWidth(),
                                onClick = {
                                    if (phone.isBlank() || username.isBlank() || password.isBlank()) {
                                        message ="لطفا اطلاعات را به درستی وارد نمایید"
                                    } else {
                                        signup = true
                                    }
                                },
                                shape = RoundedCornerShape(Dimens.textfield_corner),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Colors.appcolor

                                )
                            ) {
                                Text(
                                    text = "ایجاد حساب کاربری",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            LaunchedEffect(signup) {
                                signup = false
                                val response = RetrofitInstance.api.signUp(
                                    SignupRequest(
                                        phone = phone,
                                        username = username,
                                        password = password
                                    )
                                )
                                if (response.message == "repeated username") {
                                    message =  "نام کاربری وارد شده تکراری است"

                                } else if (response.message == "success") {

                                } else {
                                    message ="نام کاربری دیگری انتخاب کنید"

                                }
                            }

                            LaunchedEffect(message)
                            {
                                if(message.isNotBlank()) {
                                    snackScope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
    }
}


@Composable
fun SignIn(navController: NavHostController) {

    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current
    var signin by remember { mutableStateOf(false) }
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var message by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    RightToLeftLayout {

        Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) })
        {contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .background(
                        brush = Brush.linearGradient(
                            listOf(
                                Colors.signup_background_top,
                                Colors.signup_background_bottom
                            )
                        )
                    )
                    .padding(Dimens.login_padding),
                contentAlignment = Alignment.Center
            )
            {

                Card(
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = RoundedCornerShape(Dimens.login_card_corner),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    )
                    {

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = username,
                            onValueChange = { username = it },
                            shape = RoundedCornerShape(Dimens.textfield_corner),
                            placeholder = { Text(text = "نام کاربری") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall,
                            colors = TextFieldDefaults.colors(
                                focusedPlaceholderColor = Colors.outline_textfield_border,
                                unfocusedPlaceholderColor = Colors.outline_textfield_border,
                                focusedIndicatorColor = Colors.outline_textfield_border,
                                unfocusedIndicatorColor = Colors.outline_textfield_border
                            )
                        )

                        Spacer(modifier = Modifier.size(Dimens.login_spacer))


                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = password,
                            onValueChange = { password = it },
                            shape = RoundedCornerShape(Dimens.textfield_corner),
                            placeholder = { Text(text = "رمز عبور") },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodySmall,
                            colors = TextFieldDefaults.colors(
                                focusedPlaceholderColor = Colors.outline_textfield_border,
                                unfocusedPlaceholderColor = Colors.outline_textfield_border,
                                focusedIndicatorColor = Colors.outline_textfield_border,
                                unfocusedIndicatorColor = Colors.outline_textfield_border
                            )
                        )

                        Spacer(modifier = Modifier.size(Dimens.login_spacer))

                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            Button(
                                modifier = Modifier
                                    .height(Dimens.signup_button_height)
                                    .fillMaxWidth(),
                                onClick = {
                                    //keyboardController?.hide()
                                    if (username.isBlank() || password.isBlank()) {
                                        message = "لطفا تمام اطلاعات را وارد کنید"
                                    } else {
                                        signin = true
                                    }
                                },
                                shape = RoundedCornerShape(Dimens.textfield_corner),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Colors.appcolor

                                )
                            ) {
                                Text(
                                    text = "ورود",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                        if(signin) {
                            LaunchedEffect(Unit) {
                                signin = false
                                try {
                                    val response =
                                        RetrofitInstance.api.signIn(
                                            SigninRequest(
                                                username,
                                                password
                                            )
                                        )
                                    if (response.isSuccessful) {
                                        val userInfo = response.body()
                                        navController.navigate("main") {
                                            popUpTo("signin") { inclusive = true }
                                        }

                                    } else if (response.code() == 401) {
                                        message = "نام کاربری یا کلمه عبور اشتباه است!"
                                    }
                                }
                                catch(e:Exception){
                                    message = e.toString()
                                }
                            }
                        }

                        LaunchedEffect(message) {
                            if(message.isNotBlank()){
                                snackScope.launch {
                                    snackbarHostState.showSnackbar(message)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(Dimens.login_spacer))


                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            text = "رمز عبور خود را فراموش کرده ام",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center

                        )


                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignupInPreview() {
    SocialTheme {
        //SignIn()
    }
}