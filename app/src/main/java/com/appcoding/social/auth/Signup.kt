package com.appcoding.social.auth

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appcoding.social.R
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.SignupVM
import kotlinx.coroutines.launch


@Composable
fun SignUp(navController: NavHostController){

    val viewModel : SignupVM = viewModel()

    val phone by viewModel.phone.collectAsState()
    val password by viewModel.password.collectAsState()
    val username by viewModel.username.collectAsState()
    val state by viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(state.message)
    {
        if (state.success) {
            navController.navigate("signin") {
                popUpTo("signup") { inclusive = true }
            }
        } else {
            if (state.message.isNotBlank()) {
                snackScope.launch {
                    snackbarHostState.showSnackbar(state.message)
                }
            }
        }
    }

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
                        SignUpTopBar(navController)
                        Spacer(modifier = Modifier.size(15.dp))
                        ProfileImage()
                        Spacer(modifier = Modifier.size(Dimens.normal_spacer))
                        AuthTextField(phone,
                            viewModel::onPhoneChange,
                            "شماره موبایل",
                            "phone")
                        Spacer(modifier = Modifier.size(Dimens.login_spacer))
                        AuthTextField(username,
                            viewModel::onUsernameChange,
                            "نام کاربری",
                            "username")
                        Spacer(modifier = Modifier.size(Dimens.login_spacer))
                        AuthTextField(password,
                            viewModel::onPasswordChange,
                            "رمز عبور",
                            "password")
                        Spacer(modifier = Modifier.size(Dimens.login_spacer))
                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            AuthButton(text = "ایجاد حساب کاربری",
                                isLoading = state.isLoading,
                                onClick = { keyboardController?.hide()
                                viewModel.signup()}
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SignUpTopBar(navController: NavHostController){
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
                    navController.navigate("signin") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
        )
    }
}

@Composable
fun ProfileImage() {
    Icon(
        painter = painterResource(R.drawable.profile_selected),
        contentDescription = "profile image",
        tint = Colors.appcolor,
        modifier = Modifier.size(Dimens.signup_user_profile)
    )
}
