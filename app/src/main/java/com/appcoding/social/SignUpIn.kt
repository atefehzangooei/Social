package com.appcoding.social

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appcoding.social.Functions.RightToLeftLayout
import com.appcoding.social.ui.theme.SocialTheme

class SignUpIn : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialTheme {
                SignUpsignIn()
            }
        }
    }
}


@Composable
fun SignUpsignIn(){

    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    RightToLeftLayout{

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Colors.background)
            .padding(Dimens.login_padding),
            contentAlignment = Alignment.Center)
        {
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center)
            {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = phone,
                    onValueChange = { phone = it },
                    shape = RoundedCornerShape(Dimens.textfield_corner),
                    placeholder = { Text(text = "شماره موبایل") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.size(Dimens.login_spacer))

                  OutlinedTextField(
                      modifier = Modifier.fillMaxWidth(),
                      value = password,
                    onValueChange = { password = it },
                      shape = RoundedCornerShape(Dimens.textfield_corner),
                      placeholder = { Text(text = "رمز عبور") },
                    singleLine = true,
                      textStyle = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.size(Dimens.login_spacer))

                Row(modifier = Modifier.fillMaxWidth())
                {
                    Button(
                        modifier = Modifier
                            .wrapContentHeight()
                            .weight(2f),
                        onClick = {},
                        shape = RoundedCornerShape(Dimens.textfield_corner, 0.dp,
                            0.dp, Dimens.textfield_corner
                        ),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = Colors.appcolor

                        )
                    ) {
                        Text(text = "ورود",
                            style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.size(Dimens.login_spacer))

                    Button(modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                        onClick = {},
                        shape = RoundedCornerShape(0.dp, Dimens.textfield_corner,
                            Dimens.textfield_corner, 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Colors.cancel_button,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = "انصراف",
                            style = MaterialTheme.typography.bodyMedium)
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
        SignUpsignIn()
    }
}