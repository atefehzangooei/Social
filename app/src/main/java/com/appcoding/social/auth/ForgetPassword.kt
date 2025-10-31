package com.appcoding.social.auth

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.appcoding.social.screen.components.RightToLeftLayout
import com.appcoding.social.ui.theme.Colors
import com.appcoding.social.ui.theme.Dimens
import com.appcoding.social.viewmodel.ForgetPasswordVM
import kotlinx.coroutines.launch


@Composable
fun ForgetPassword(navController: NavHostController) {

    val viewModel : ForgetPasswordVM = viewModel()

    val phone by viewModel.phone.collectAsState()
    val username by viewModel.username.collectAsState()
    val state by viewModel.state.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.success) {
        if(state.success){
            navController.navigate("signin"){
                popUpTo("forgetpassword"){ inclusive = true }
            }
        }
        else {
            if (state.message.isNotBlank()) {
                snackScope.launch {
                    snackbarHostState.showSnackbar(state.message)
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
                        AuthTextField(username,viewModel::onUsernameChanged,"نام کاربری")
                        Spacer(modifier = Modifier.size(Dimens.login_spacer))
                        AuthTextField(phone, viewModel::onPhoneChanged, "شماره موبایل")
                        Spacer(modifier = Modifier.size(Dimens.login_spacer))

                        Row(modifier = Modifier.fillMaxWidth())
                        {
                            AuthButton(text = "بازیابی رمز عبور",
                                isLoading = state.isLoading,
                                onClick = {
                                    keyboardController?.hide()
                                    viewModel.forgetPassword()
                                })
                        }
                    }
                }

            }

        }
    }
}
