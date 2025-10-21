package com.appcoding.social.splash

import android.os.Bundle
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.R
import com.appcoding.social.screen.AppNavHost
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.ui.theme.SocialTheme
import com.appcoding.social.viewmodel.ForgetPasswordVM
import com.appcoding.social.viewmodel.SigninVM
import com.appcoding.social.viewmodel.SignupVM
import com.appcoding.social.viewmodel.SplashVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
fun Splash(navController: NavHostController){

    val viewModel : SplashVM = hiltViewModel()
    val userid by viewModel.userid.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.appcolor)
    )

    //val userid = 1
    LaunchedEffect(Unit) {
        delay(3000)
        if(userid > 0){
            navController.navigate("main/$userid"){
                popUpTo("splash"){inclusive = true}
            }
        }
        else {
            navController.navigate("signin") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
}




@Composable
fun ForgetPassword(navController: NavHostController) {

    val viewModel : ForgetPasswordVM = viewModel()

    val phone by viewModel.phone.collectAsState()
    val username by viewModel.username.collectAsState()
    val message by viewModel.message.collectAsState()
    val success by viewModel.success.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(success) {
        if(success){
            navController.navigate("signin"){
                popUpTo("forgetpassword"){ inclusive = true }
            }
        }
        else {
            if (message.isNotBlank()) {
                snackScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }

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
                            onValueChange = { viewModel.onUsernameChanged(it) },
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
                            value = phone,
                            onValueChange = { viewModel.onPhoneChanged(it) },
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

                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            Button(
                                modifier = Modifier
                                    .height(Dimens.signup_button_height)
                                    .fillMaxWidth(),
                                onClick = {
                                    keyboardController?.hide()
                                    viewModel.forgetPassword()
                                },
                                shape = RoundedCornerShape(Dimens.textfield_corner),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    containerColor = Colors.appcolor

                                )
                            ) {
                                if(isLoading){
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                                else {
                                    Text(
                                        text = "بازیابی رمز عبور",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }



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